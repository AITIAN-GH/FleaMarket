<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="loadPermissionList">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
            confirm-button-text='确定'
            cancel-button-text='取消'
            icon-color="#626AEF"
            width="156px"
            title="删除这些角色？"
            @confirm="delBatch">
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
      <el-table-column align="center" prop="name" label="名称"></el-table-column>
      <el-table-column align="center" prop="path" label="访问路径">
        <template #default="scope">
          {{ scope.row.path ? scope.row.path : '-'  }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="page" label="组件名称">
        <template #default="scope">
          {{ scope.row.page ? scope.row.page : '-' }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="icon" label="组件图标">
        <template #default="scope">
          <el-icon><component :is="scope.row.icon"/></el-icon>
        </template>
      </el-table-column>
      <el-table-column width="280" align="center">
        <template #header>
          <el-input v-model="search" placeholder="当前页搜索(名称)" />
        </template>
        <template #default="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon-color="#626AEF"
                title="确定删除？"
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
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          v-model:current-page="pageIndex"
          :page-sizes="[2, 5, 10, 20]"
          v-model:page-size="pageSize"
          layout="total, sizes, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <!-- 添加权限菜单信息 -->
    <el-dialog title="权限菜单信息" v-model="dialogFormVisible" width="30%" >
      <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="访问路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="组件名称">
          <el-input v-model="form.page" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="组件图标">
          <el-dropdown trigger="click" @command="changeIcon">
            <div>
              <component :is="form.icon" class="myIcon"></component>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu style="height: 300px;overflow: auto;">
                <el-dropdown-item 
                  :icon="iconitem.code" 
                  :command="iconitem.value" 
                  v-for="iconitem in iconList" :key="iconitem.id">
                  {{ iconitem.value }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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
import { ref, onMounted, computed } from 'vue'
import { getPagePermissions, savePermissioin, delPermissioins, delOnePermissioin } from '../api/permission.js'
import { loadIcons } from '../api/dict.js'

const dialogFormVisible = ref(false)
const name = ref('')
const search = ref('')
const multipleSelection = ref([])
const tableData = ref([])
const pageIndex = ref(1)
const pageSize = ref(5)
const total = ref(0)
const iconList = ref([])
const form = ref({
  name: '',
  path: '',
  page: '',
  icon: '',
})

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.name.toLowerCase().includes(search.value.trim().toLowerCase())
))

const save = () => {
  savePermissioin(form.value).then(res => {
    if (res.code === '200') {
      console.log("保存成功")
      loadPermissionList()
    } else {
      ElNotification.error({
        title: '通 知',
        message: '保存失败 !!!',
        duration: 1200,
        showClose: false
      })
    }
  })
  dialogFormVisible.value = false
}

const handleAdd = () => {
  dialogFormVisible.value = true
  loadIconList()
  form.value = {
    name: '',
    path: '',
    page: '',
    icon: 'house'
  }
}

const handleEdit = (row) => {
  form.value = row
  loadIconList()
  dialogFormVisible.value = true
}

const del = (id) => {
  console.log(id)
  delOnePermissioin(id).then(res => {
    if (res.code === '200') {
      loadPermissionList()
    } else {
      ElNotification.error({
        title: '通 知',
        message: '删除失败 !!!',
        duration: 1200,
        showClose: false
      })
    }
  })
}

// 处理表单多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(item => item.id)
  console.log(multipleSelection.value)
}

// 加载所有角色信息
const loadPermissionList = async () => {
  let ret = await getPagePermissions({
    pageSize: pageSize.value,
    pageIndex: pageIndex.value,
    name: name.value
  })
  tableData.value = ret.data.records
  total.value = ret.data.total
}

const delBatch = () => {
  if (multipleSelection.value.length > 0) {
    delPermissioins(multipleSelection.value).then(res => {
      if (res.code === '200') {
        loadPermissionList()
      } else {
        ElNotification.error({
          title: '通 知',
          message: '批量删除失败 !!!',
          duration: 1200,
          showClose: false
        })
      }
    })
  }else(
    ElNotification.error({
      title: '通 知',
      message: '请选择要删除的数据 !!!',
      duration: 1200,
      showClose: false
    })
  )
}

// // 处理分页
const handleSizeChange = (ps) => {
  pageIndex.value = 1
  pageSize.value = ps
  loadPermissionList()
}

const handleCurrentChange = (pn) => {
  pageIndex.value = pn
  loadPermissionList()
}

const reset = () => {
  name.value = ""
  loadPermissionList()
}

// 加载所有图标信息
const loadIconList = () => {
  loadIcons().then(res => {
    iconList.value = res.data
  }).catch(err => {
    console.log(err)
  })
}

const changeIcon = (icon) =>{
  form.value.icon = icon
}

onMounted(() => {
  loadPermissionList()
})
</script>

<style scoped>
.ml-5 {
  margin-left: 5px;
}
.headerBg {
  background: #eee!important;
}
.myIcon{
  width: 1.5em; 
  height: 1.5em; 
  margin-right: 8px;
  color:#123456;
}
</style>
