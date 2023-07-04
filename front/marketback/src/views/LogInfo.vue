<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
            <el-button style="margin-left: 10px;" type="primary" @click="loadSystemLog">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
            <el-button type="success" @click="downloadLog" v-if="filterTableData.length > 0">下载日志</el-button>
            <el-button type="success" @click="downloadBySelected" v-if="multipleSelection.length > 0">下载选中</el-button>
        </div>

        <el-table :default-sort="{ prop: 'id', order: 'descending' }" :data="filterTableData" border
            :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
            <el-table-column type="selection" align="center" width="80"></el-table-column>
            <el-table-column prop="id" align="center" sortable label="ID" width="100"></el-table-column>
            <el-table-column prop="userName" align="center" width="240" label="用户名称"></el-table-column>
            <el-table-column prop="ip" align="center" width="240" label="IP地址"></el-table-column>
            <el-table-column prop="status" align="center" label="登录状态"></el-table-column>
            <el-table-column align="center">
                <template #header>
                    <div style="width: 260px;">
                        <div style="margin-left: 20px;">
                            <el-input v-model="search" placeholder="当前页搜索(用户名称)" />
                        </div>
                    </div>
                </template>
                <template #default="scope">
                    {{ new Date(scope.row.lastTime).toLocaleString() }}
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

    </div>
</template>
  
<script setup>
import { ElNotification } from 'element-plus'
import { ref, onMounted, computed } from 'vue'
import { getSystemLog } from '../api/dict.js'

const name = ref('')
const search = ref('')
const multipleSelection = ref([])
const tableData = ref([])
const pageIndex = ref(1)
const pageSize = ref(5)
const total = ref(0)

const filterTableData = computed(() =>
    tableData.value.filter((data) => !search.value.trim() || data.userName.toLowerCase().includes(search.value.trim().toLowerCase())
))

// 处理表单多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val.map(item => item.id)
}

// 加载所有字典信息
const loadSystemLog = () => {
    getSystemLog({
        pageSize: pageSize.value,
        pageIndex: pageIndex.value,
        value: name.value
    }).then(ret => {
        tableData.value = ret.data.records
        total.value = ret.data.total
    }).catch(err => {
        console.log(err)
    })
}

// 下载日志信息
const downloadLog = () => {
    const link = document.createElement('a')
    link.href = 'http://localhost:8090/file/systemlog/download'
    link.target = '_self'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

// 下载日志信息
const downloadBySelected = () => {
    if(!multipleSelection.value.length > 0){
        ElNotification.success({
            title: '通 知',
            message: '请先勾选要下载的数据 !!!',
            duration: 1200,
            showClose: false
        })   
        return
    }
    let ids = []
    multipleSelection.value.forEach(item => {
        ids.push(item)
    })
    const link = document.createElement('a')
    link.href = 'http://localhost:8090/file/systemlog/download?ids='+ids.join(',')
    link.target = '_self'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

// 处理分页
const handleSizeChange = (ps) => {
    pageIndex.value = 1
    pageSize.value = ps
    loadSystemLog()
}

const handleCurrentChange = (pn) => {
    pageIndex.value = pn
    loadSystemLog()
}

const reset = () => {
    name.value = ''
    loadSystemLog()
}

onMounted(() => {
    loadSystemLog()
})

</script>