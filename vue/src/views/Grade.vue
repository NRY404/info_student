<template>
    <div>
        <!--        顶部搜索框 -->
        <div style="margin: 10px 0">
            <el-input class="ml-5" v-model="studentId" style="width: 200px;" suffix-icon="el-icon-edit" placeholder="请输入学号"></el-input>
            <el-button style="margin-left: 5px;" type="primary" icon="el-icon-search" @click="selectByCondition">搜索</el-button>
            <el-button style="margin-left: 5px;margin-right: 15px;" type="warning"  @click="reset">重置</el-button>
            <el-button type="info" @click="exportGeade" class="ml-5" round>导出 <i class="el-icon-upload2"></i></el-button>
        </div>


        <el-table
                :data="tableData" border stripe
                :header-cell-class-name="headerBg">
            <!--            多选框-->
            <el-table-column
                    type="selection"
                    align="center"
                    width="50">
            </el-table-column>
            <el-table-column  prop="studentId" align="center" label="学号" width="120"></el-table-column>
            <el-table-column prop="name" align="center" label="姓名"></el-table-column>
            <el-table-column prop="courseName" align="center" label="课程名"></el-table-column>
            <el-table-column prop="courseType" align="center" label="课程类别"></el-table-column>
            <el-table-column prop="courseMark" align="center" label="课程学分"></el-table-column>
            <el-table-column prop="grade" align="center" label="成绩"></el-table-column>

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
</template>

<script>
    export default {
        name: "File",
        data(){
            return{
                tableData: [],
                studentId: '',
                name: '',
                courseName: '',
                courseType: '',
                courseMark: '',
                grade: '',
                pageNum: 1,
                pageSize: 10,
                total: 0,
                headerBg:'headerBg'
            }
        },
        mounted() {
            this.load();
        },

        methods:{

            load(){
                this.request.get("/grade/page",{
                    params: {
                        pageNum:this.pageNum,
                        pageSize:this.pageSize
                    }
                }).then(req => {
                        this.tableData = req.data;
                        this.total = req.total;

                })

            },

            //分页
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                if (this.studentId ==null || this.studentId === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }

            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                if (this.studentId ==null || this.studentId === ''){
                    this.load();
                }else {
                    this.selectByCondition();
                }
            },

            handleFileUploadSuccess(){
                this.load();
            },

            selectByCondition(){
                this.request.get("/grade/page3?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&studentId="+this.studentId).then(req => {
                    this.tableData = req.data;
                    this.total = req.total;
                })
            },
            //重置
            reset(){
                this.studentId = '';
                this.load();
            },

            exportGeade(){
                window.open("http://localhost:9090/grade/export");
            }

        }
    }
</script>

<style scoped>

</style>