<template>
    <div>
        <div style="margin: 10px 0;display: flex;align-items: center;">
            <el-button type="primary" @click="handleAdd" v-if="userAuths.includes('pro.add')">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" width="156px"
                title="删除这些商品？" @confirm="delBatch" v-if="userAuths.includes('comment.deleteBatch')">
                <template #reference>
                    <el-button type="danger">删除</el-button>
                </template>
            </el-popconfirm>
            <div style="position: absolute;right: 120px;">
                <el-text size="large" tag="b">商品名：</el-text>
                <el-text size="large" tag="b">{{ currActive.currentProductName }}</el-text>
            </div>
        </div>
        <el-table 
            :default-sort="{ prop: 'id', order: 'descending' }"
            :data="filterTableData" border stripe 
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" v-if="userAuths.includes('comment.deleteBatch')"></el-table-column>
            <el-table-column prop="id" label="ID" sortable width="80"></el-table-column>
            <el-table-column width="140px"  prop="fromName" label="评论者"></el-table-column>
            <el-table-column prop="content" label="评论"></el-table-column>
            <el-table-column width="210px" prop="time" label="时间"></el-table-column>
            <el-table-column width="230px" align="center">
                <template #header>
                    <el-input v-model="search" placeholder="当前页搜索(评论者)" />
                </template>
                <template #default="scope">
                    <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' icon-color="#626AEF" title="确定删除？"
                        @confirm="del(scope.row.id)"  v-if="userAuths.includes('comment.delete')">
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

        <!-- 添加评论信息 -->
        <el-dialog title="评论" v-model="addFormVisible" width="30%">
            <el-form label-width="80px">
                <el-form-item label="你的评论">
                    <el-input v-model="form.content" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div>
                    <el-button @click="addFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="save">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { getOneComment, addComment, delComment } from '../api/goods.js'
import { ref, onMounted, computed } from 'vue'
import { userInfo, userPageActive } from '../store'
import { useRoute } from 'vue-router'

const currActive = userPageActive()
const route = useRoute()
const userStore = userInfo()
const search = ref('')
const total = ref(0)
const pageSize = ref(5)
const pageIndex = ref(1)
const userAuths = ref([])
const tableData = ref([])
const addFormVisible = ref(false)
const multipleSelection = ref([])

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.fromName.toLowerCase().includes(search.value.trim().toLowerCase())
))

const form = ref({
    productId: 0,
    content: '',
    userId: userStore.user.id
})

const save = () => {
    addComment(form.value).then(res => {
        console.log(res)
        loadCommentList()
    }).catch((err) => {
        ElNotification.error({
            title: '通 知',
            message: '更新失败 !!!',
            duration: 1200,
            showClose: false
        })
        console.log(err)
    })
    addFormVisible.value = false
}

const handleAdd = () => {
    addFormVisible.value = true
    form.value = {
        productId: currActive.currentProductId != 0 ? parseInt(currActive.currentProductId) : 1,
        content: '',
        userId: userStore.user.id
    }
}

// 处理表单多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val.map(item => item.id)
}

// 加载用户商品信息
const loadCommentList = () => {
    // 从router中获取productId
    if (route.query.productId) {
        currActive.setPageId(route.query.productId)
        currActive.setProductName(route.query.productName)
        getOneComment({
            productId: route.query.productId,
            pageIndex: pageIndex.value,
            pageSize: pageSize.value
        }).then(ret => {
            tableData.value = ret.data.commentList
            total.value = ret.data.total
        }).catch(err => { 	
            console.log(err) 
        })
    }else if(currActive.currentProductId != 0) {
        getOneComment({
            productId: currActive.currentProductId,
            pageIndex: pageIndex.value,
            pageSize: pageSize.value
        }).then(ret => {
            tableData.value = ret.data.commentList
            total.value = ret.data.total
        }).catch(err => { 	
            console.log(err) 
        })
    }else {
        getOneComment({
            productId: 1,
            pageIndex: pageIndex.value,
            pageSize: pageSize.value
        }).then(ret => {
            tableData.value = ret.data.commentList
            total.value = ret.data.total
        }).catch(err => { 	
            console.log(err) 
        })
    }
}

const delBatch = () => {
    if (multipleSelection.value.length > 0) {
        console.log(multipleSelection.value)
        delComment({
            productId: parseInt(currActive.currentProductId),
            delIds: multipleSelection.value
        }).then((res) => {
            console.log(res)
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

const del = (id) => {
    console.log(id)
    delComment({
        productId: parseInt(currActive.currentProductId),
        delIds: id
    }).then((res) => {
        console.log(res)
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


// // 处理分页
const handleSizeChange = (ps) => {
    pageIndex.value = 1
    pageSize.value = ps
    loadCommentList()
}

const handleCurrentChange = (pn) => {
    pageIndex.value = pn
    loadCommentList()
}

onMounted(() => {
    loadCommentList()
    userAuths.value = userStore.user.auths
})
</script> 

<style lang="scss" scoped>
::v-deep .el-table td, ::v-deep.el-table th {
    text-align: center;
}
</style>
    