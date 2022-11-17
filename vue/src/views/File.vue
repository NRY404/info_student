<template>
    <div>
        <!--        顶部搜索框 -->
        <div style="margin: 10px 0">
            <el-input class="ml-5" v-model="name" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入文件名"></el-input>
            <el-button style="margin-left: 5px;" type="primary" icon="el-icon-search" @click="load">搜索</el-button>
            <el-button style="margin-left: 5px;" type="warning"  @click="reset">重置</el-button>
        </div>
<!--        功能区 上传文件 批量删除-->
        <div style="margin: 10px 0">
            <el-upload action="http://localhost:9090/file/upload"
                       :show-file-list="false"
                       :on-success="handleFileUploadSuccess"
                       style="display: inline-block;margin-left: 5px;">
                <el-button type="info" round>上传文件<i class="el-icon-upload"></i></el-button>
            </el-upload>
            <el-popconfirm
                    confirm-button-text='好的'
                    cancel-button-text='不用了'
                    icon="el-icon-info"
                    icon-color="red"
                    title="是否批量确认删除?"
                    @confirm="deleteByIds"
            >
                <el-button type="danger" slot="reference" class="ml-5">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
        </div>

        <el-table
                :data="tableData" border stripe
                :header-cell-class-name="headerBg"
                @selection-change="handleSelectionChange">
            <!--            多选框-->
            <el-table-column
                    type="selection"
                    align="center"
                    width="50">
            </el-table-column>
            <el-table-column  prop="id" align="center" label="ID" width="40"></el-table-column>
            <el-table-column prop="name" align="center" label="文件名称"></el-table-column>
            <el-table-column prop="type" align="center" label="文件类型"></el-table-column>
            <el-table-column prop="size" align="center" label="文件大小(kb)"></el-table-column>
            <el-table-column align="center" label="下载">
                <template slot-scope="scope">
                    <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="enable" align="center" label="启用">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable"
                               active-color="#13ce66"
                               inactive-color="#ccc"
                               @change="changeEnable(scope.row)"

                    ></el-switch>
                </template>
            </el-table-column>
            <el-table-column fixed="right"
                             label="操作"
                             align="center"
                             width="150">
                <template slot-scope="scope">

                    <el-popconfirm
                            confirm-button-text='好的'
                            cancel-button-text='不用了'
                            icon="el-icon-info"
                            icon-color="red"
                            title="是否确认删除?"
                            @confirm="handleDelete(scope.row.id)"
                    >
                        <el-button style="margin-left: 2px;"
                                   size="mini"
                                   type="danger"
                                   slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>

            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <!--
            current-page：当前页码
            page-sizes：分页条件数
            page-size：分页大小-->
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "File",
        data(){
            return{
                tableData: [],
                multipleSelection: [],
                pageNum: 1,
                pageSize: 5,
                total: 0,
                name: '',
                isDelete: false,
                headerBg:'headerBg'
            }
        },
        mounted() {
            this.load();
        },

        methods:{

            load(){
                this.request.get("/file/page",{
                    params: {
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                        name:this.name
                    }
                }).then(req => {
                        this.tableData = req.data.records;
                        this.total = req.data.total;

                })

            },
            //复选框选中后执行的方法
            handleSelectionChange(val) {
                this.multipleSelection = val;
                console.log(this.multipleSelection);

            },
            //分页
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                if (this.name ==null || this.name === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }

            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                if (this.name ==null || this.name === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },

            handleFileUploadSuccess(){
                this.load();
            },


            //重置
            reset(){
                this.name = '';
                this.load();
            },
            //文件下载
            download(url){
                window.open(url)
            },

            //设置 状态
            changeEnable(row){
                this.request.post("/file/enable",row).then(res => {
                    if (res.code === '200'){
                        this.$message.success("操作成功");

                    }else{
                        this.$message.error("操作失败");
                    }
                })
            },

            //数据删除
            handleDelete(id) {
                this.request("/file/delete/"+id).then(resp => {
                    if (resp.code === '200'){
                        //    删除成功
                        this.$message.success("删除成功");
                        this.load();
                    }else {
                        this.$message.error("删除失败");
                    }
                })
            },

            //批量删除
            deleteByIds(){
                // 将对象类型 直接转为 id数组
                // [{},{},{}] => {1,2,3}
                let ids =  this.multipleSelection.map(v => v.id);
                this.request.post("/file/deletes",ids).then(resp => {
                    if (resp){
                        //    删除成功
                        this.load();
                        this.$message({
                            message: '批量删除成功!',
                            type: 'success'
                        });
                    }else {
                        this.$message.error("删除失败");
                    }
                })
            },

        }
    }
</script>

<style scoped>

</style>