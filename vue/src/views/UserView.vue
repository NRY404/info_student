<template>
    <div>
<!--        顶部搜索框 -->
        <div style="margin: 10px 0">
            <el-input class="ml-5" v-model="studentId" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入学号"></el-input>
            <el-input class="ml-5" v-model="name" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入姓名"></el-input>
            <el-input class="ml-5" v-model="email" style="width: 200px;" suffix-icon="el-icon-message" placeholder="请输入邮箱"></el-input>
            <el-button style="margin-left: 5px;" type="primary" icon="el-icon-search" @click="selectByCondition">搜索</el-button>
            <el-button style="margin-left: 5px;" type="warning"  @click="reset">重置</el-button>
        </div>

        <!--            新增 dialog-->
        <div style="margin: 10px 0">
            <!--    添加数据的表单-->
            <el-dialog
                    :title="formTitle"
                    :visible.sync="dialogVisible"
                    width="30%">
                <el-form ref="form" :model="form" label-width="80px" size="small">
                    <el-form-item label="头像" style="line-height: 50px;height: 50px">
                        <el-upload
                                class="avatar-uploader"
                                action="http://localhost:9090/file/upload"
                                accept=".jpg,.jpeg,.png,.JPG,.JPEG"
                                :show-file-list="false"
                                :on-success="handleAvatarSuccess"
                                :before-upload="beforeAvatarUpload">
                            <img style="width: 50px;border-radius: 50%;  left: 35px; cursor: pointer;
                                        display: block;
                                        box-shadow: 3px 5px 5px rgba(135,206,235, 0.3);"
                                 v-if="form.avatar" :src="form.avatar" alt="头像" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="学号">
                        <el-input v-model="form.studentId"></el-input>
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-select v-model="form.gender" placeholder="请选择" @change="changeCName">
                            <el-option
                                    v-for="item in genderN"
                                    :key="item.gType"
                                    :label="item.label"
                                    :value="item.label">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="班级">
                        <el-select v-model="form.className" placeholder="请选择" @change="changeCName">
                            <el-option
                                    v-for="item in classN"
                                    :key="item.classId"
                                    :label="item.label"
                                    :value="item.classId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="宿舍">
                        <el-select v-model="form.dormName" placeholder="请选择" @change="changeDName">
                            <el-option
                                    v-for="item in dorm"
                                    :key="item.dormId"
                                    :label="item.label"
                                    :value="item.dormId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="电话">
                        <el-input v-model="form.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="form.email"></el-input>
                    </el-form-item>
                    <el-form-item label="出生日期">
                        <el-date-picker
                                v-model="form.birthday"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="地址">
                        <el-input type="textarea" v-model="form.address"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="addStudent">提交</el-button>
                        <el-button @click="dialogVisible = false" >取消</el-button>
                    </el-form-item>
            </el-form>
            </el-dialog>
        </div>
<!--        功能区-新增、删除、导出、导入-->
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd" plain>新增 <i class="el-icon-circle-plus-outline"></i></el-button>
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
            <el-upload action="http://localhost:9090/student/import"
                       :show-file-list="false"
                       accept=".xlsx,.xls"
                       :on-success="handleImportSuccess"
                       style="display: inline-block;margin-left: 5px;">
                <el-button type="info" round>导入<i class="el-icon-download"></i></el-button>
            </el-upload>
            <el-button type="info" @click="exportStudents" class="ml-5" round>导出 <i class="el-icon-upload2"></i></el-button>
        </div>
<!--    数据区-->
        <el-table
                :data="tableData" border stripe
                :header-cell-class-name="headerBg"
                :row-class-name="tableRowClassName"
                @selection-change="handleSelectionChange">
            <!--            多选框-->
            <el-table-column type="selection" align="center" width="50"></el-table-column>
            <el-table-column  prop="studentId" align="center" label="学号" width="100"></el-table-column>
            <el-table-column prop="name" align="center" label="姓名" width="120"></el-table-column>
            <el-table-column prop="gender" align="center" label="性别" width="50"></el-table-column>
            <el-table-column prop="className" align="center" label="班级"></el-table-column>
            <el-table-column prop="dormName" align="center" label="宿舍"></el-table-column>
            <el-table-column prop="phone" align="center" label="电话"></el-table-column>
            <el-table-column prop="email" align="center" label="邮箱"></el-table-column>
            <el-table-column prop="birthday" align="center" label="出生日期"></el-table-column>
            <el-table-column prop="address" align="center" label="地址"></el-table-column>
            <el-table-column fixed="right"
                             label="操作"
                             align="center"
                             width="150">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            @click="handleEdit(scope.row)">编辑</el-button>
                    <el-popconfirm
                            confirm-button-text='好的'
                            cancel-button-text='不用了'
                            icon="el-icon-info"
                            icon-color="red"
                            title="是否确认删除?"
                            @confirm="handleDelete(scope.row.studentId)"
                    >
                        <el-button style="margin-left: 2px;"
                                   size="mini"
                                   type="danger"
                                   slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>

            </el-table-column>
        </el-table>
<!--        分页-->
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
        name: "StudentView",
        props:{
            isCollapse: Boolean,
            logoTextShow: Boolean
        },
        data(){
            return{
                // 多选框数据
                multipleSelection:[],
                tableData: [],  //表单数据
                total:0,  //总条数
                pageNum: 1,
                pageSize: 10,
                dialogVisible:false,
                form: { },
                dormL: { },
                classL: { },
                formTitle: '',
                studentId:'',
                name:'',
                gender:'',
                classId: '',
                className:'',
                dormName:'',
                dormId: '',
                phone: '',
                email:'',
                birthday:'',
                address:'',
                avatar: '',
                headerBg:'headerBg',
                genderN: [
                    {
                      gType: '0',
                      label: '男'
                    },
                    {
                      gType: '1',
                      label: '女'
                    },
                    {
                      gType: '2',
                      label: '零'
                    }
                    ],
                dorm: [{
                    dormId: '101',
                    label: '公寓01-101'
                }],
                classN: [{
                    classId: '2019101',
                    label: '磨角193'
                }],
            }
        },

        mounted() {
            this.load();
        },

        methods:{

            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
            },
            load(){
                this.request.get("/student/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize).
                then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                });
                this.request.get("/class/").then(req => {
                    this.classN = req;
                    let i = 0;
                    for (i = 0; i < req.length; i++) {
                        this.classN[i].classId = req[i].classId;
                        this.classN[i].label = req[i].className;
                    }
                });
                this.request.get("/dorm/").then(req => {
                    this.dorm = req;
                    let i = 0;
                    for (i = 0; i < req.length; i++) {
                        this.dorm[i].dormId = req[i].dormId;
                        this.dorm[i].label = req[i].dormName;
                    }
                });
            },

            //复选框选中后执行的方法
            handleSelectionChange(val) {
                this.multipleSelection = val;
                console.log(this.multipleSelection);
            },//分页
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                if (this.studentId ==null && this.name == null && this.email == null){
                    this.load();
                }else {
                    this.selectByCondition();
                }

            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.selectByCondition();
            },

            //表格数据操作
            handleEdit(row) {
                // this.form = row;
                this.form = Object.assign({},row); //将row拷贝到空对象中，解决没点确定 表中数据改变的问题
                this.dialogVisible = true;
                this.formTitle = '修改学生信息';
            },

            //数据删除
            handleDelete(studentId) {
                this.$confirm("此操作永久删除当前数据","删除",{
                    type:'info'
                }).then(()=>{
                    this.request("/student/delete/"+studentId).then(resp => {
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

            //批量删除
            deleteByIds(){
                // 将对象类型 直接转为 id数组
                // [{},{},{}] => {1,2,3}
                let studentIds =  this.multipleSelection.map(v => v.studentId);
                this.request.post("/student/deletes",studentIds).then(resp => {
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

            selectByCondition(){

                this.request.get("/student/page3?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&studentId="+this.studentId+"&name="+this.name+"&email="+this.email).then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
                //  请求分页查询数据
                // fetch("http://localhost:9090/user/page3?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email+"&address="+this.address).then(res => res.json()).then(res =>{
                //     console.log(res);
                //     this.tableData = res.data;
                //     // this.tableData = res.records;
                //     this.total = res.total;
                // })

            },

            //重置
            reset(){
                this.studentId = '';
                this.name = '';
                this.email = '';
                this.load();
            },

            //新增按钮
            handleAdd(){
                this.dialogVisible = true;
                this.formTitle = '添加学生';
                this.form = {};
            },
            //新增数据表单创建方法 保存
            addStudent(){

                this.request.post("/student/add",this.form).then(resp => {
                    if (resp){
                        //判断宿舍是否已满员
                        if(resp.code === '123'){
                            this.$message({
                                message: resp.msg,
                                type: 'warning'
                            });
                            return;
                        }

                        //    添加成功
                        //    关闭窗口
                        this.dialogVisible = false;
                        this.form.avatar = resp.avatar;
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

            //变换班级号
            changeCName(classId){
                this.form.classL = this.classN.find(item => item.classId === classId);
                this.form.classId = this.form.classL.classId;
                this.form.className = this.form.classL.label;
            },
            //变换宿舍号
            changeDName(dormId){
                this.form.dormL = this.dorm.find(item => item.dormId === dormId);
                this.form.dormId = this.form.dormL.dormId;
                this.form.dormName = this.form.dormL.label;
            },

//          头像上传成功
            handleAvatarSuccess(res) {
                this.form.avatar = res;
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg' || 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isJPG && isLt2M;
            },

        //    导入
            handleImportSuccess(){
                this.$message.success("文件上传成功");
                this.load();
            },

        //    导出
            exportStudents(){
                window.open("http://localhost:9090/student/export");
            }

        }
    }
</script>

<style>
    .headerBg{
        background-color: #d7dde5 !important;
    }
    .avatar-uploader {
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        border: 1px dotted #95b5ea;
        box-shadow: 3px 5px 5px rgba(135,206,235, 0.3);
    }
    .avatar-uploader {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 26px;
        color: #8c939d;
        width: 50px;
        height: 50px;
        line-height: 50px;
        text-align: center;
    }
</style>