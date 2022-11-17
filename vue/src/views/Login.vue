<template>
    <div class="wrapper">
        <div style="margin: 200px auto; background-color: rgba(255,250,250,0.3); width: 350px; height: 300px;padding: 20px;border-radius: 10px">
            <div style="margin: 20px 0;text-align: center;color: rgba(255,250,250,0.7); font-size: 24px"><b>登 录</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
<!--        v-model双向绑定，会根据你输入的值，自动传入到form中进行校验，
            要不然它拿不到你输入的值，所以就一直提示你没填信息
            vue 使用DOM对象，ref -->
                <el-form-item  prop="username">
                    <el-input size="medium"  style=" opacity: 0.5; margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item  prop="password">
                    <el-input type="password"  size="medium" style=" opacity: 0.5; margin: 10px 0" prefix-icon="el-icon-lock" v-model="user.password" show-password placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: center">
                    <el-button type="primary" @click="LoginIn" size="small" autocomplete="off">登录</el-button>
                    <el-button type="primary" @click="$router.push('/register')" size="small" autocomplete="off">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data(){
            return{
                user:{
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
                        {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods:{
            LoginIn(){
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {    //合法
                        this.request.post("/user/login",this.user).then(res => {
                            if (res.code === '200'){
                                localStorage.setItem("userIn",JSON.stringify(res.data));//将用户信息存入到浏览器
                                this.$router.push("/home");
                                this.$message.success("登录成功")
                            }else {
                                this.$message.error(res.meg)
                            }
                        })
                    }
                });
            }
        }
    }

</script>

<style>
    .wrapper{
        height: 100vh;
        background-image: linear-gradient(to bottom right, #f69d92,#87CEFA);
        overflow: hidden;
    }
</style>