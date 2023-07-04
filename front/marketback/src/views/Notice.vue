<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="loadNoticeList">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" width="156px"
                title="删除这些角色？" @confirm="delBatch">
                <template #reference>
                    <el-button type="danger">删除</el-button>
                </template>
            </el-popconfirm>
        </div>

        <el-table 
            :default-sort="{ prop: 'id', order: 'descending' }"
            :data="filterTableData" border stripe 
            @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"></el-table-column>
            <el-table-column align="center" prop="id" width="68px" sortable label="ID"></el-table-column>
            <el-table-column align="center" prop="title" label="活动标题"></el-table-column>
            <el-table-column align="center" prop="content" label="活动内容"></el-table-column>
            <el-table-column align="center" width="160px" label="开始时间">
                <template #default="scope">
                    {{ new Date(scope.row.createTime).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column width="160px" label="结束时间">
                <template #default="scope">
                    {{ new Date(scope.row.endTime).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column width="280" align="center">
                <template #header>
                    <el-input v-model="search" placeholder="当前页搜索(标题)" />
                </template>
                <template #default="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" title="确定删除？"
                        @confirm="del(scope.row.id)">
                        <template #reference>
                            <el-button type="danger">删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div style="padding: 10px 0">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                v-model:current-page="pageIndex" :page-sizes="[2, 5, 10, 20]" v-model:page-size="pageSize"
                layout="total, sizes, prev, pager, next" :total="total">
            </el-pagination>
        </div>

        <!-- 添加公告信息 -->
        <el-dialog title="新的公告信息" v-model="dialogFormVisible" width="30%">
            <el-form label-width="80px">
                <el-form-item label="活动标题">
                    <el-input v-model="form.title" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="活动内容">
                    <el-input v-model="form.content" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="起始时间">
                    <el-date-picker 
                        v-model="form.createTime" type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择起始时间" style="width: 100%"/>
                </el-form-item>
                <el-form-item label="截止时间">
                    <el-date-picker 
                        v-model="form.endTime" type="date" 
                        value-format="YYYY-MM-DD"
                        placeholder="请选择截止时间" style="width: 100%"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="save">确 定</el-button>
                </span>
            </template>

        </el-dialog>
    </div>
</template>
  
<script setup>
import { ElNotification } from 'element-plus'
import { getAllNotice, delNotice, updateNotice } from '../api/notice.js'
import { ref, onMounted, computed } from 'vue'

const dialogFormVisible = ref(false)
const name = ref('')
const search = ref('')
const multipleSelection = ref([])
const tableData = ref([])
const pageIndex = ref(1)
const pageSize = ref(5)
const total = ref(0)

const form = ref({
    id: 0,
    title: '',
    content: '',
    createTime: '',
    endTime: ''
})

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.title.toLowerCase().includes(search.value.trim().toLowerCase())
))

const save = () => {
    if (form.value.title === '' || form.value.content ==='' || form.value.createTime > form.value.endTime) {
        ElNotification.warning({
            title: '通 知',
            message: '请正确完善信息!!!',
            duration: 1200,
            showClose: false
        })
        return
    }
    updateNotice(form.value).then(res => {
        if (res.code == '200') {
            loadNoticeList()
        }else{
            ElNotification.error({
                title: '通 知',
                message: '更新失败 !!!',
                duration: 1200,
                showClose: false
            })
        }
    }).catch(err => {
        ElNotification.error({
            title: '通 知',
            message: '更新失败 !!!',
            duration: 1200,
            showClose: false
        })
        console.log(err)
    })
    dialogFormVisible.value = false
}


const handleAdd = () => {
    dialogFormVisible.value = true
    form.value = {
        id: 0,
        title: '',
        content: '',
        createTime: '',
        endTime: ''
    }
}

const handleEdit = (row) => {
    form.value.id = row.id
    form.value.title = row.title
    form.value.content = row.content
    form.value.createTime = getTime(row.createTime)
    form.value.endTime = getTime(row.endTime)
    dialogFormVisible.value = true
}

// 处理表单多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val.map(item => item.id)
}

const del = (id) => {
    delNotice(id).then(res => {
        if (res.code == '200') {
            loadNoticeList()
        } else {
            ElNotification.error({
                title: '通 知',
                message: '删除失败 !!!',
                duration: 1200,
                showClose: false
            })
        }
    }).catch(err => {
        console.log(err)
        ElNotification.error({
            title: '通 知',
            message: '删除出错 !!!',
            duration: 1200,
            showClose: false
        })
        
    })
}

const delBatch = () => {
    if (multipleSelection.value.length > 0) {
        delNotice(multipleSelection.value).then(res => {
            if (res.code == '200') {
                loadNoticeList()
            } else {
                ElNotification.error({
                    title: '通 知',
                    message: '删除失败!!!',
                    duration: 1200,
                    showClose: false
                })
            }
        }).catch(err => {
            console.log(err)
            ElNotification.error({
                title: '通 知',
                message: '删除出错!!!',
                duration: 1200,
                showClose: false
            })
        })
    } else {
        ElNotification.warning({
            title: '通 知',
            message: '请选择要删除的数据 !!!',
            duration: 1200,
            showClose: false
        })
    }
}

// 处理分页
const handleSizeChange = (ps) => {
    pageIndex.value = 1
    pageSize.value = ps
    loadNoticeList()
}

const handleCurrentChange = (pn) => {
    pageIndex.value = pn
    loadNoticeList()
}

const reset = () => {
    name.value = ""
    loadNoticeList()
}

// 加载所有公告信息
const loadNoticeList = () => {
    getAllNotice({
        title: name.value,
        pageIndex: pageIndex.value,
        pageSize: pageSize.value
    }).then(res => {
        tableData.value = res.data.records
        total.value = res.data.total
    }).catch(err => {
        console.log(err)
    })


}

//时间生成并处理
const getTime = (time) => {
  // 对应的方法
  const timeType = [
    "getFullYear",
    "getMonth",
    "getDate",
    "getHours",
    "getMinutes",
    "getSeconds"
  ]
  // 分隔符
  const separator = {
    getFullYear: "-",
    getMonth: "-",
    getDate: " ",
    getHours: ":",
    getMinutes: ":",
    getSeconds: ""
  }
  let resStr = ""
  for (let i = 0; i < timeType.length; i++) {
    const element = timeType[i];
    let resTime = new Date(time)[element]();
    // 获取月份的要+1
    resTime = element == "getMonth" ? resTime + 1 : resTime;
    // 小于10，前面加0
    resTime = resTime > 9 ? resTime : "0" + resTime;
    resStr = resStr + resTime + separator[element];
  }
  return resStr
}

onMounted(() => {
    loadNoticeList()
})
</script>


<style scoped>
.ml-5 {
    margin-left: 5px;
}
</style>
  