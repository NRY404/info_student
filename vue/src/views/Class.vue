<template>
    <div>
        <div style="height:360px; overflow:auto;">
            <!--        顶部搜索框 -->
            <div style="margin: 10px 0">
                <el-input class="ml-5" v-model="className" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入班级名"></el-input>
                <el-button style="margin-left: 5px;" type="primary" icon="el-icon-search" @click="selectByCondition">搜索</el-button>
                <el-button style="margin-left: 5px;margin-right: 15px;" type="warning"  @click="reset">重置</el-button>

                <el-button type="primary" @click="handleAdd" plain>新增 <i class="el-icon-circle-plus-outline"></i></el-button>
                <el-upload action="http://localhost:9090/class/import"
                           :show-file-list="false"
                           accept=".xlsx,.xls"
                           :on-success="handleImportSuccess"
                           style="display: inline-block;margin-left: 5px;">
                    <el-button type="info" round>导入<i class="el-icon-download"></i></el-button>
                </el-upload>
                <el-button type="info" @click="exportClass" class="ml-5" round>导出 <i class="el-icon-upload2"></i></el-button>
            </div>

            <el-table
                    :data="tableData" border stripe
                    :header-cell-class-name="headerBg">
                <el-table-column  prop="classId" align="center" label="班级号" width="100"></el-table-column>
                <el-table-column prop="className" align="center" label="班级名"></el-table-column>
                <el-table-column prop="speciality" align="center" label="专业"></el-table-column>
                <el-table-column prop="counsellor" align="center" label="辅导员"></el-table-column>
                <el-table-column prop="phone" align="center" label="联系电话"></el-table-column>
                <el-table-column prop="entranceYear" align="center" label="创建年份"></el-table-column>

                <el-table-column fixed="right"
                                 label="操作"
                                 align="center"
                                 width="150">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="handleEdit(scope.row)">{{edit}}</el-button>
                        <el-popconfirm
                                confirm-button-text='好的'
                                cancel-button-text='不用了'
                                icon="el-icon-info"
                                icon-color="red"
                                title="是否确认删除?"
                                @confirm="handleDelete(scope.row.classId)"
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
                        :page-sizes="[5]"
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
                    <el-form-item label="班级号">
                        <el-input v-model="form.classId"></el-input>
                    </el-form-item>
                    <el-form-item label="班级名">
                        <el-input v-model="form.className"></el-input>
                    </el-form-item>
                    <el-form-item label="专业">
                        <el-input v-model="form.speciality"></el-input>
                    </el-form-item>
                    <el-form-item label="辅导员">
                        <el-input v-model="form.counsellor"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="创建年份">
                        <el-input v-model="form.entranceYear"></el-input>
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
                classId: '',
                className: '',
                speciality: '',
                counsellor: '',
                phone: '',
                entranceYear: '',
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
                this.request.get("/class/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize).
                then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
            },
            //新增按钮
            handleAdd(){
                this.dialogVisible = true;
                this.formTitle = '添加班级';
                this.form = {};
            },
            addClas(){
                this.request.post("/class/add",this.form).then(resp => {
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
                if (this.className == null || this.className === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                if (this.className == null || this.className === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },
            //表格数据操作
            handleEdit(row) {
                this.request.get("/class/getStu?pageNumS="+this.pageNumS+"&pageSizeS="+this.pageSizeS+"&classId="+row.classId).
                then(req => {
                    console.log(req.data)
                    if(req.data == null || req.data.length === 0){
                        this.$message({
                            message: '该班级未加入学生！',
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
                this.request.get("/class/page3?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&className="+this.className).then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
            },
            //重置
            reset(){
                this.className = '';
                this.load();
            },
            //文件下载
            download(url){
                window.open(url)
            },

//数据删除
            handleDelete(classId) {
                this.$confirm("此操作永久删除当前数据","删除",{
                    type:'info'
                }).then(()=>{
                    this.request("/class/delete/"+classId).then(resp => {
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
            exportClass(){
                window.open("http://localhost:9090/class/export");
            }

        }

    }
</script>

<style scoped>

</style>