<template>
    <el-menu :default-openeds="['1', '3']" class="el-submenu_title"
             style="background-image: linear-gradient(to bottom right, #f69d92,#87CEFA);
             height: 100%;overflow-x: hidden;"
             :collapse="isCollapse"
             :collapse-transition="false"
             text-color="#000000"
             background-color="rgba(0,0,0,0.1)"
             @select="handleSelect"
             router
    >   <!-- router 声明 路由模式-->
        <div style="height: 60px;line-height: 60px;text-align: center">
            <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px;">
            <b v-show="logoTextShow" style="color: aliceblue">学生信息查询系统</b>
        </div>

        <el-menu-item index="/home">
            <!-- 由于在index.js 中 已经为路由 / 重定向到 /home，所以该项会访问该 路径-->
            <template slot="title">
                <i class="el-icon-s-home"></i>
                <span slot="title">主页</span>
            </template>
        </el-menu-item>
        <el-submenu index="2" >
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">系统管理</span>
            </template>
            <el-menu-item-group style="background-color: rgba(135,206,235, 0.3)">
                <template slot="title" >分组一</template>
                <el-menu-item index="/userview">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-user-solid"></i>
                        <span slot="title">学生管理</span>
                    </template>
                </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group style="background-color: rgba(135,206,235, 0.3)">
                <template slot="title" >分组二</template>
                <el-menu-item index="/class">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-office-building"></i>
                        <span slot="title">班级管理</span>
                    </template>
                </el-menu-item>
                <el-menu-item index="/dorm">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-school"></i>
                        <span slot="title">宿舍管理</span>
                    </template>
                </el-menu-item>
                <el-menu-item index="/grade">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-data-line"></i>
                        <span slot="title">成绩管理</span>
                    </template>
                </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group style="background-color: rgba(135,206,235, 0.3)">
                <template slot="title" >分组三</template>
                <el-menu-item index="/file">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-files"></i>
                        <span slot="title">文件管理</span>
                    </template>
                </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group style="background-color: rgba(135,206,235, 0.3)">
                <template slot="title" >分组四</template>
                <el-menu-item index="/echart">    <!-- index 用于指定 路由-->
                    <template slot="title">
                        <i class="el-icon-pie-chart"></i>
                        <span slot="title">图表</span>
                    </template>
                </el-menu-item>
            </el-menu-item-group>
        </el-submenu>

    </el-menu>
</template>

<script>
    export default {
        name: "Aside",
        props:{
            isCollapse: Boolean,
            logoTextShow: Boolean
        },
        methods:{
            handleSelect(index){
                console.log(index);
                console.log(this.$router.options.routes)

            }
        }

    }
</script>

<style scoped>

</style>