<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="loadDictList">搜索</el-button>
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
            :header-cell-class-name="'headerBg'"
            @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"></el-table-column>
            <el-table-column align="center" prop="id" sortable label="ID" width="80"></el-table-column>
            <el-table-column align="center" prop="value" label="名称"></el-table-column>
            <el-table-column align="center" prop="icon" label="组件图标">
                <template #default="scope">
                    <el-icon>
                        <component :is="scope.row.code" />
                    </el-icon>
                </template>
            </el-table-column>
            <el-table-column align="center" prop="type" label="类型"></el-table-column>
            <el-table-column width="280" align="center">
                <template #header>
                    <el-input v-model="search" placeholder="当前页搜索(名称)" />
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

        <!-- 编辑字典信息 -->
        <el-dialog title="字典信息" v-model="dialogFormVisible" width="30%">
            <el-form label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="form.value" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="组件图标">
                    <el-input v-model="form.code" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="组件类型">
                    <el-input v-model="form.type" autocomplete="off"></el-input>
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
import { getAllDicts, updateDict, delDictById, delDictByIds } from '../api/dict.js'
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
    value: '',
    code: '',
    type: ''
})

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.value.toLowerCase().includes(search.value.trim().toLowerCase())
))

const save = () => {
    if (form.value.name === '' ||form.value.flag ==='' ) {
        ElNotification.warning({
            title: '通 知',
            message: '请完善信息!!!',
            duration: 1200,
            showClose: false
        })
        return
    }
    form.value.value = form.value.value.trim()
    form.value.code = form.value.code.trim()
    form.value.type = form.value.type.trim()
    updateDict(form.value).then(res => {
        if(res.code == '200'){
            loadDictList()
        }else{
            ElNotification.error({
                title: '通 知',
                message: '操作失败 !!!',
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
        value: '',
        code: '',
        type: ''
    }
}

const handleEdit = (row) => {
    form.value = row
    dialogFormVisible.value = true
}

const del = (id) => {
    delDictById(id).then(res => {
        if(res.code == '200'){
            loadDictList()
        }else{
            ElNotification.error({
                title: '通 知',
                message: '删除出错 !!!',
                duration: 1200,
                showClose: false
            })
        }
    }).catch(err => {
        ElNotification.error({
            title: '通 知',
            message: '删除出错 !!!',
            duration: 1200,
            showClose: false
        })
        console.log(err)
    })
    loadDictList()
}

// 处理表单多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val.map(item => item.id)
}

// 加载所有字典信息
const loadDictList = async () => {
    let ret = await getAllDicts({
        pageSize: pageSize.value,
        pageIndex: pageIndex.value,
        value: name.value
    })
    tableData.value = ret.data.records
    total.value = ret.data.total
}

 const delBatch = () => {
    if (multipleSelection.value.length > 0) {
        delDictByIds(multipleSelection.value).then(res => {
            loadDictList()
        }).catch(err => {
            ElNotification.error({
                title: '通 知',
                message: '删除出错!!!',
                duration: 1200,
                showClose: false
            })
            console.log(err)
        })
    } else (
        console.log("请选择要删除的数据")
    )
    loadDictList()
}

// 处理分页
const handleSizeChange = (ps) => {
    pageIndex.value = 1
    pageSize.value = ps
    loadDictList()
}

const handleCurrentChange = (pn) => {
    pageIndex.value = pn
    loadDictList()
}

const reset = () => {
    name.value = ""
    loadDictList()
}

onMounted(() => {
    loadDictList()
})
</script>


<style scoped>
.ml-5 {
    margin-left: 5px;
}

.headerBg {
    background: #eee !important;
}
</style>
  