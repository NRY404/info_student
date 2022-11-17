<template>
    <div>
        <el-row>
            <el-col :span="12">
                <div id="main" style="height: 400px;width: 500px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="pie" style="height: 400px;width: 500px;"></div>
            </el-col>
        </el-row>
    </div>
</template>




<script>
    import * as echarts from 'echarts';

    export default {
        name: "Echarts",
        data(){
            return{

            }
        },
        mounted() {
            //线状图
            var option= {
                title: {
                    text: "男女比例表",
                    left: 'center'
                },
                xAxis: {
                    type: 'category',
                    data: ['男同学','女同学','ZERO']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        data: [],
                        type: 'line'
                    }
                ]
            };
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);

            //饼图
            var pieOption =  {
                title: {
                    text: '男女比例图',
                    subtext: '',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        name: 'uh~huh',
                        type: 'pie',
                        radius: '50%',
                        label: {
                            normal: {
                                show: true,
                                position: "inner",
                                textStyle: {
                                    fontWeight: 200,
                                    fontSize: 16, //文字的字体大小
                                    color: "#fff"
                                },
                                formatter: "{d}%" /*饼状图内显示百分比*/,
                                // data:['40%','60%'],
                            },
                            data: [],
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    }
                ]
            };
            var pieDom = document.getElementById('pie');
            var myPie = echarts.init(pieDom);

            //从后台获取图表数据
            this.request.get("/echarts/members").then(res => {
                option.series[0].data = res.data;
                //在数据准备完毕后，再进行set
                myChart.setOption(option);

                pieOption.series[0].data = [
                    { name: "男同学", value: res.data[0] },
                    { name: "女同学", value: res.data[1] },
                    { name: "ZERO", value: res.data[2] },
                ];
                myPie.setOption(pieOption);
            });







        }
    }
</script>

<style scoped>

</style>