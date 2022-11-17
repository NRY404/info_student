<template>
    <div style=" font-size: 12px;display: flex; ">
        <div style="flex: 1;font-size: 20px;">
            <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
        </div>
        <div style="text-align: right;font-size: 12px;width: 200px;"></div>
        <el-dropdown style="width: 120px;cursor: pointer">
            <div style="display: inline-block">
                <el-avatar style="position: relative;top: 10px;right: 15px;" :size="40" shape="circle" :src="userIn.avatar" alt=""></el-avatar>
                <i class="el-icon-setting" style="margin-right: 15px"><span>{{ userIn.nickname}}</span></i>
            </div>
            <el-dropdown-menu style="margin-right: 30px" slot="dropdown" >
                <el-dropdown-item>
                    <el-button style=" font-size: 14px; padding: 5px 0; text-decoration: none;color: #B3C0D1;border: none;"
                            @click="handleEdit()">个人信息</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                    <el-button style=" font-size: 14px; padding: 5px 0; text-decoration: none;color: #B3C0D1;border: none;" @click="loginOut">退出</el-button>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        <div style="margin: 10px 0">
            <!--    查看个人信息 的表单-->
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
                            <el-avatar style="border-radius: 50%;  left: 35px; cursor: pointer;
                                        display: block;
                                        box-shadow: 3px 5px 5px rgba(135,206,235, 0.3);"
                                       shape="circle" :size="50" :fit="fill"
                                       v-if="form.avatar" :src="form.avatar" alt="头像" class="avatar" ></el-avatar>
                            <i v-else class="el-icon-plus avatar-uploader-icon" ></i>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input v-model="form.username" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="form.password" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="昵称">
                        <el-input v-model="form.nickname" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="电话">
                        <el-input v-model="form.phone" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="form.email" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="地址">
                        <el-input type="textarea" v-model="form.address" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="uploadAvatar" >OK</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
    </div>

</template>

<script>
    export default {
        name: "Header",
        data(){
          return{
              formTitle: '',
              form: { },
              dialogVisible: false,
              userIn:localStorage.getItem("userIn") ? JSON.parse(localStorage.getItem("userIn")) : {}
          }
        },
        props: {
            collapseBtnClass: String,
            collapse: Function
        },
        created() {
            this.getUser();
        },
        methods: {
            loginOut() {
                this.$router.push("/login");
                localStorage.removeItem("userIn");
                this.$message.success("退出成功");
            },
            handleEdit() {
                this.form = this.userIn; //将row拷贝到空对象中，解决没点确定 表中数据改变的问题
                this.dialogVisible = true;
                this.formTitle = '个人信息';
            },

//          头像上传成功
            handleAvatarSuccess(res) {
                this.form.avatar = res;
            },
            getUser(row){

            },
            uploadAvatar(){
                this.request.post("/user/add",this.form).then(resp => {
                    if (resp){
                        //    添加成功
                        //    关闭窗口
                        this.dialogVisible = false;
                        this.$message({
                            message: '操作成功!',
                            type: 'success'
                        });
                    }else {
                        this.$message.error("保存失败");
                    }


                });
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
            }
        }
    }
</script>

<style scoped>
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