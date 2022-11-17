<template>
    <div>
        <div style="height:360px; overflow:auto;">
            <!--        顶部搜索框 -->
            <div style="margin: 10px 0">
                <el-input class="ml-5" v-model="dormName" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入宿舍名"></el-input>
                <el-button style="margin-left: 5px;" type="primary" icon="el-icon-search" @click="selectByCondition">搜索</el-button>
                <el-button style="margin-left: 5px;margin-right: 15px;" type="warning"  @click="reset">重置</el-button>

                <el-button type="primary" @click="handleAdd" plain>新增 <i class="el-icon-circle-plus-outline"></i></el-button>
                <el-upload action="http://localhost:9090/dorm/import"
                           :show-file-list="false"
                           accept=".xlsx,.xls"
                           :on-success="handleImportSuccess"
                           style="display: inline-block;margin-left: 5px;">
                    <el-button type="info" round>导入<i class="el-icon-download"></i></el-button>
                </el-upload>
                <el-button type="info" @click="exportDorm" class="ml-5" round>导出 <i class="el-icon-upload2"></i></el-button>
            </div>

            <el-table
                    :data="tableData" border stripe
                    :header-cell-class-name="headerBg">
                <el-table-column  prop="dormId" align="center" label="宿舍号" width="100"></el-table-column>
                <el-table-column prop="dormName" align="center" label="宿舍名"></el-table-column>
                <el-table-column prop="telephone" align="center" label="联系电话"></el-table-column>

                <el-table-column fixed="right"
                                 label="操作"
                                 align="center"
                                 width="150">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="handleEdit(scope.row)">{{edit}}</el-button>
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

        <!--            新增 dialog-->
        <div style="margin: 10px 0">
            <!--    添加数据的表单-->
            <el-dialog
                    :title="formTitle"
                    :visible.sync="dialogVisible"
                    width="30%">
                <el-form ref="form" :model="form" label-width="80px" size="small">
                    <el-form-item label="宿舍号">
                        <el-input v-model="form.dormId"></el-input>
                    </el-form-item>
                    <el-form-item label="宿舍名">
                        <el-input v-model="form.dormName"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.telephone"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="addClas">提交</el-button>
                        <el-button @click="dialogVisible = false" >取消</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
        <div style="border:1px solid #cccccc;height:500px;overflow:auto;"
             v-show="show"
            >
            <el-table
                    :data="studentData" border stripe
                    :header-cell-class-name="headerBg" >
                <el-table-column  prop="studentId" align="center" label="学号" width="150"></el-table-column>
                <el-table-column prop="name" align="center" label="学生名" width="150"></el-table-column>
            </el-table>
            <div style="padding: 10px 0">
                <!--
                current-page：当前页码
                page-sizes：分页条件数
                page-size：分页大小-->
                <el-pagination
                        @size-change="handleSizeChangeStu"
                        @current-change="handleCurrentChangeStu"
                        :current-page="pageNumS"
                        :page-sizes="[2, 5, 10, 20]"
                        :page-size="pageSizeS"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalS">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Class",
        data(){
            return{
                tableData: [],
                studentData: [],
                form: { },
                formTitle: '',
                dialogVisible: false,
                dormId: '',
                dormName: '',
                telephone: '',
                studentId: '',
                name: '',
                pageNum: 1,
                pageSize: 5,
                pageNumS: 1,
                pageSizeS: 10,
                total: 0,
                totalS: 0,
                edit: '查看',
                show: false,
                headerBg:'headerBg'
            }
        },
        mounted() {
            this.load();
        },

        methods:{

            load(){
                this.request.get("/dorm/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize).
                then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
            },
            //新增按钮
            handleAdd(){
                this.dialogVisible = true;
                this.formTitle = '添加宿舍';
                this.form = {};
            },
            addClas(){
                this.request.post("/dorm/add",this.form).then(resp => {
                    if (resp){
                        //
                        if(resp.code === '124'){
                            this.$message({
                                message: resp.msg,
                                type: 'warning'
                            });
                            return;
                        }

                        //    添加成功
                        //    关闭窗口
                        this.dialogVisible = false;
                        this.load();
                        this.$message({
                            message: '操作成功!',
                            type: 'success'
                        });
                    }else {
                        this.$message.error("保存失败");
                    }
                });
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                if (this.dormName == null || this.dormName === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                if (this.dormName == null || this.dormName === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },
            //表格数据操作
            handleEdit(row) {
                this.request.get("/dorm/getStu?pageNumS="+this.pageNumS+"&pageSizeS="+this.pageSizeS+"&dormId="+row.dormId).
                then(req => {
                    if(req.data == null || req.data.length === 0){
                        this.$message({
                            message: '该宿舍未加入学生！',
                            type: 'warning'
                        });
                    }else{
                        this.studentData = req.data;
                        this.show = !this.show;
                        this.totalS = req.total;
                    }
                });
            },
            handleSizeChangeStu(pageSize) {
                this.pageSizeS = pageSize;
                this.handleEdit(this.studentData[0]);
                this.show = false;
            },
            handleCurrentChangeStu(pageNum) {
                this.pageNumS = pageNum;
                this.handleEdit(this.studentData[0]);
                this.show = false;
            },

            selectByCondition(){
                this.request.get("/dorm/page3?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&dormName="+this.dormName).then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
            },
            //重置
            reset(){
                this.dormName = '';
                this.load();
            },
            //文件下载
            download(url){
                window.open(url)
            },

//数据删除
            handleDelete(dormId) {
                this.$confirm("此操作永久删除当前数据","删除",{
                    type:'info'
                }).then(()=>{
                    this.request("/dorm/delete/"+dormId).then(resp => {
                        if (resp){
                            // //    删除成功
                            // this.load();
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            });
                        }else {
                            this.$message.error("删除失败");
                        }
                    }).finally(()=>{
                        this.load();
                    });
                }).catch(()=>{
                    this.$message.info("取消删除");
                });
            },

            //    导入
            handleImportSuccess(){
                this.$message.success("文件上传成功");
                this.load();
            },

            //    导出
            exportDorm(){
                window.open("http://localhost:9090/dorm/export");
            }

        }

    }
</script>

<style scoped>

</style>