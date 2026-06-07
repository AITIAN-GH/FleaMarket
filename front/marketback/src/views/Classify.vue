<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="loadClassifyList">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" width="156px"
                title="删除这些分类？" @confirm="delBatch">
                <template #reference>
                    <el-button type="danger">删除</el-button>
                </template>
            </el-popconfirm>
        </div>

        <el-table :default-sort="{ prop: 'id', order: 'descending' }" :data="filterTableData" border stripe
            @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"></el-table-column>
            <el-table-column align="center" prop="bid" sortable label="ID" width="80"></el-table-column>
            <el-table-column align="center" prop="classify" label="分类名称"></el-table-column>
            <el-table-column align="center" prop="path" label="访问路径"></el-table-column>
            <el-table-column align="center" width="280">
                <template #header>
                    <el-input v-model="search" placeholder="当前页搜索(名称)" />
                </template>
                <template #default="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" title="确定删除？"
                        @confirm="del(scope.row.bid)">
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

        <!-- 添加角色信息 -->
        <el-dialog title="分类信息" v-model="dialogFormVisible" width="30%">
            <el-form label-width="80px">
                <el-form-item label="分类名称">
                    <el-input v-model="form.classify" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="访问路径">
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div>
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="save">确 定</el-button>
                </div>
            </template>
        </el-dialog>

    </div>
</template>
  
<script setup>
import { getClassifyData, updateClassify, deleteClassify, deleteBathClassify } from '../api/classify.js'
import { ElNotification } from 'element-plus'
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
    bid: 0,
    classify: '',
    path: '',
})

const filterTableData = computed(() =>
    tableData.value.filter((data) =>
    !search.value.trim() || data.classify.toLowerCase().includes(search.value.trim().toLowerCase())
))

const save = () => {
    updateClassify(form.value).then(res => {
        if (res.code == '200') {
            loadClassifyList()
        }
    }).catch((err) => {
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
    form.value = {
        bid: 0,
        classify: '',
        path: ''
    }
    dialogFormVisible.value = true
}

const handleEdit = (row) => {
    form.value = row
    dialogFormVisible.value = true
}

const del = (id) => {
    deleteClassify(id).then((res) => {
        if(res.code == '200'){
            loadClassifyList()
        }
    }).catch((err) => {
        ElNotification.error({
            title: '通 知',
            message: '删除出错 !!!',
            duration: 1200,
            showClose: false
        })
        console.log(err)
    })
}

// 加载所有用户信息
const loadClassifyList = () => {
    getClassifyData({
        pageSize: pageSize.value,
        pageIndex: pageIndex.value,
        name: name.value.trim()
    }).then(ret => {
        if (ret.code == '200') {
            tableData.value = ret.data.records
            total.value = ret.data.total
        }
    }).catch(err => {
        console.log(err)
    })

}

const delBatch = () => {
    if (multipleSelection.value.length > 0) {
        deleteBathClassify(multipleSelection.value).then((res) => {
            if(res.code == '200'){
                loadClassifyList()
            }
        }).catch((err) => {
            ElNotification.error({
                title: '通 知',
                message: '删除出错 !!!',
                duration: 1200,
                showClose: false
            })
            console.log(err)
        })
    } else (
        ElNotification.warning({
            title: '通 知',
            message: '请选择要删除的数据 !!!',
            duration: 1200,
            showClose: false
        })
    )
}

// 处理表单多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val.map(item => item.bid)
}

// 处理分页
const handleSizeChange = (ps) => {
    pageSize.value = ps
    loadClassifyList()
}

const handleCurrentChange = (pn) => {
    pageIndex.value = pn
    loadClassifyList()
}

const reset = () => {
    name.value = ""
    loadClassifyList()
}

onMounted(() => {
    loadClassifyList()
})
</script>
  
  
<style scoped>
.ml-5 {
    margin-left: 5px;
}
</style>
    