<template>
    <div class="home" style="height: 100%;">
        <el-container style="height: 100%;">
            <!--      左侧导航栏-->
            <el-aside class="el-aside" :width="sideWidth+'px'" style=" height: 100%;">
                <Aside :is-collapse="isCollapse" :logoTextShow="logoTextShow"></Aside>
            </el-aside>

            <!--      右侧内容-->
            <el-container>
                <!--        头部-->
                <el-header class="el-header">
                    <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse"></Header>
                </el-header>

                <!--        面包屑-->
                <div class="example-container">
                    <el-breadcrumb separator=">" style="margin-top: 10px;">

                    </el-breadcrumb>
                </div>
                <!--        右侧列表-->
                <el-main>
<!--                    表示当前页面的子路由 会在 router-view 里面显示-->
                    <router-view />
                </el-main>

            </el-container>
        </el-container>
    </div>
</template>

<style>
    .el-header {
        background-image: linear-gradient(to bottom right, #f69d92,#87CEFA);
        color: #333;
        line-height: 60px;
        border-left: 1px dotted rgba(135,206,235, 0.8);
    }
    .el-aside{
        box-shadow: 1px 1px 3px rgba(135,206,235, 0.8);
    }

</style>

<script>

    // import request from "../utils/request";
    import Aside from "../components/Aside";
    import Header from "../components/Header";
    import UserView from "./UserView";


    export default {
        name: 'HomeManage',
        data(){
            return{
                collapseBtnClass:'el-icon-s-fold',//收缩图标
                isCollapse:false,
                sideWidth:200,
                logoTextShow: true,

            }
        },

        //
        components:{
            Aside,
            Header,
            UserView
        },
        //
        methods:{
            //点击收缩按钮，触发
            collapse(){
                this.isCollapse = !this.isCollapse;
                if (this.isCollapse){//收缩
                    this.sideWidth = 64;
                    this.logoTextShow = false;
                    this.collapseBtnClass = 'el-icon-s-unfold'

                }else{    //展开
                    this.sideWidth = 200;
                    this.logoTextShow = true;
                    this.collapseBtnClass = 'el-icon-s-fold'
                }
            }

         },



    }
</script>
