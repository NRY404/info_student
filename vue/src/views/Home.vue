<template>
    <div style="margin-left: 190px;">
        <h1 style="font-size: 70px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">
            学生信息查询系统<span style="font-size: 30px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">(Student Information Query System)</span>
        </h1>
        <span style="font-size: 50px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">欢迎您！{{userIn.nickname}}</span>
        <!-- date -->
        <br />
        <br />
        <div class="timeBox padding-xl">
            <p class="text-24 text-white" v-html="formateTimeStamp(date)"></p>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
            return{
                date: new Date().getTime(),
                userIn:localStorage.getItem("userIn") ? JSON.parse(localStorage.getItem("userIn")) : {}
            }
        },
        mounted: function() {

        },
        created: function() {
            this.loadTime()
        },
        methods: {
            loadTime() {
                var _this = this;
                setInterval(() => {
                    _this.date += 1000;
                }, 1000);

            },
            // 转换时间戳
            formateTimeStamp(time) {
                var date = new Date(time);
                var year = date.getFullYear();
                var month =
                    date.getMonth() + 1 < 10
                        ? "0" + (date.getMonth() + 1)
                        : date.getMonth() + 1;
                var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
                var minute =
                    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
                var second =
                    date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
                var week = ["日", "一", "二", "三", "四", "五", "六"][date.getDay()];
                // return year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second;
                return (
                    '<span style="font-size: 30px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">' + year + "年" + month + "月" + day + "日" +
                    "&nbsp;&nbsp;&nbsp;星期" + week + '<br />'+'<span style="font-size: 40px; text-shadow: 0px 2px 7px rgba(51, 51, 51, 0.6);">' +
                    hour +
                    ":" +
                    minute + ":" + second +
                    '</span><br/>'
                );
            },
        }



    }

</script>

<style scoped>

</style>