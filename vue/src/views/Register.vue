<template>
    <div class="wrapper">
        <div style="margin: 100px auto; background-color: rgba(255,250,250,0.3); width: 350px; height: 530px;padding: 20px;border-radius: 10px">
            <div style="margin: -5px 0 20px 0;text-align: center;color: rgba(255,250,250,0.7); font-size: 24px"><b>注
                册</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
                <el-form-item prop="username">
                    <el-input size="medium" style=" opacity: 0.5; margin: 5px 0" prefix-icon="el-icon-user"
                              v-model="user.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" size="medium" style=" opacity: 0.5; margin: 5px 0"
                              prefix-icon="el-icon-user" v-model="user.password" placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item prop="nickname">
                    <el-input size="medium" style=" opacity: 0.5; margin: 5px 0" prefix-icon="el-icon-user"
                              placeholder="请输入昵称" v-model="user.nickname"></el-input>
                </el-form-item>
                <el-form-item prop="phone">
                    <el-input size="medium" style=" opacity: 0.5; margin: 5px 0" prefix-icon="el-icon-user"
                              placeholder="请输入电话" v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input size="medium" style=" opacity: 0.5; margin: 5px 0" prefix-icon="el-icon-user"
                              placeholder="请输入邮箱" v-model="user.email"></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: center">
                    <el-button type="primary" @click="addUser" size="small" autocomplete="off">提交</el-button>
                    <el-button type="primary" size="small" @click="$router.push('/')" autocomplete="off">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Register",
        data() {
            const validateNum = (rule, value, callback) => {
                debugger
                const phone = /^1\d{10}$/    //以1开头的数字
                //const phone= /^[0-9]*$/
                // console.log(phone.test(value))
                // console.log(value)
                if (!(phone.test(value)) && value) {
                    callback(new Error('手机号格式不正确'))
                } else {
                    callback()
                }
            };

            return {
                user: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 3, max: 11, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    nickname: [
                        {required: true, message: '请输入昵称', trigger: 'blur'},
                        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '请输入手机号', trigger: 'blur'},
                        {min: 11, max: 11, message: '请输入11位手机号', trigger: 'blur'},
                        {validator: validateNum, trigger: "blur"}
                    ],
                    email: [
                        {required: true, message: '请输入邮箱', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    address: [
                        {required: true, message: '请输入地址', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            addUser() {
                if ((this.user.username !== null && this.user.username !== "") &&
                    (this.user.password !== null && this.user.password !== "") &&
                    (this.user.nickname !== null && this.user.nickname !== "") &&
                    (this.user.phone !== null && this.user.phone !== "") &&
                    (this.user.email !== null && this.user.email !== "") &&
                    (this.user.address !== null && this.user.address !== ""))
                {
                    this.request.post("/user/register", this.user).then(resp => {
                        if (resp) {
                            //    添加成功
                            this.$message({
                                message: '注册成功!',
                                type: 'success'
                            });
                            this.$router.push("/");
                        } else {
                            this.$message.error("注册失败");
                        }

                    });
                }else {
                    this.$message.error("请填写完整信息！");
                }

            },
            // cancel() {
            //     this.$router.push("/");
            // }

        }
    }
</script>

<style>
    .wrapper {
        height: 100vh;
        background-image: linear-gradient(to bottom right, #f69d92, #87CEFA);
        overflow: hidden;
    }
</style>