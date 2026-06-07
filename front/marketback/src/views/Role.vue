<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="loadRoleList">搜索</el-button>
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
      <el-table-column align="center" prop="flag" label="唯一标识"></el-table-column>
      <el-table-column align="center" width="280">
        <template #header>
          <el-input v-model="search" placeholder="当前页搜索(名称)" />
        </template>
        <template #default="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i></el-button>
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

    <!-- 添加角色信息 -->
    <el-dialog title="角色信息" v-model="dialogFormVisible" width="30%" >
      <el-form label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
        
    </el-dialog>

    <el-dialog title="权限菜单分配" v-model="menuDialogVis" width="30%">
      <el-tree
          :data="menuData"
          show-checkbox
          :props="treeProps"
          node-key="id"
          ref="tree"
          :default-checked-keys="checks">
          <template #default="{data}">
            <span class="custom-tree-node">
              <span><el-icon><component :is="data.icon"/></el-icon> {{ data.name }}</span>
            </span>
          </template>
      </el-tree>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="menuDialogVis = false">取 消</el-button>
          <el-button type="primary" @click="saveRoleMenu()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { getRoleList, updateRole, delRoleById, delRoleByIds, getPermissionsByRoleId } from '../api/role.js'
import { getAllPermissions, saveRolePermissions } from '../api/permission.js'
import { ref, onMounted, computed } from 'vue'
import { userInfo } from '../store'

const userStore = userInfo()
const dialogFormVisible = ref(false)
const menuDialogVis = ref(false)
const name = ref('')
const search = ref('')
const multipleSelection = ref([])
const tableData = ref([])
const menuData = ref([])
const checks = ref([])
const tree = ref([])
const pageIndex = ref(1)
const pageSize = ref(5)
const total = ref(0)
const roleId = ref(0)
const roleFlag = ref('')

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.name.toLowerCase().includes(search.value.trim().toLowerCase())
))

const form = ref({
  name: '',
  flag: ''
})

const treeProps = ref({
  label: 'name',
  children: 'children',
})

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
  updateRole(form.value).then(res => {
    loadRoleList()
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
    name: '',
    flag: ''
  }
}

const del = (id) => {
  delRoleById(id).then(res => {
    console.log(res)
    loadRoleList()
  }).catch(err => {
    ElNotification.error({
      title: '通 知',
      message: '删除出错 !!!',
      duration: 1200,
      showClose: false
    })
    console.log(err)
  })
}

const delBatch = () => {

  if (multipleSelection.value.length > 0) {

    delRoleByIds(multipleSelection.value).then(res => {
      console.log(res)
      loadRoleList()
    }).catch(err => {
      ElNotification.error({
        title: '通 知',
        message: '删除出错 !!!',
        duration: 1200,
        showClose: false
      })
      console.log(err)
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

const handleEdit = (row) => {
  form.value = row
  dialogFormVisible.value = true
}

// 加载所有角色信息
const loadRoleList = async () => {
  let ret = await getRoleList({
    pageSize: pageSize.value,
    pageIndex: pageIndex.value,
    name: name.value
  })
  if(ret.data != null){
    tableData.value = ret.data.records
    total.value = ret.data.total
  }
}

// 处理表单多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(item => item.id)
}

// 处理分页
const handleSizeChange = (ps) => {
  pageSize.value = ps
  loadRoleList()
}

const handleCurrentChange = (pn) => {
  pageIndex.value = pn
  loadRoleList()
}

// 菜单树
const selectMenu = (role) => {
  checks.value = []
  roleId.value = role.id
  roleFlag.value = role.flag

  menuDialogVis.value = true
  
  // 初始化菜单树
  getAllPermissions().then(res => {
    menuData.value = res.data
  })
  
  // 请求角色菜单数据
  getPermissionsByRoleId(role.id).then((res) => {
    // 拿到权限列表
    let userpermissions = res.data.map(item => item.id)
    // 筛选出小于10的父级权限id列表
    let temp1 = userpermissions.filter(item => item < 10).map(item => item)
    // 筛选出大于10的子级权限id列表
    let temp2 = userpermissions.filter(item => item > 10).map(item => item)
    checks.value = userpermissions.filter(item => !temp1.includes(item) || !temp2.some(em => parseInt(em/10) == item))
  })
}

const saveRoleMenu = () => {
  let data = {
    menuIds: [],
    roleId: roleId.value
  }
  let changedKeys = []
  tree.value.getHalfCheckedKeys().forEach(item => changedKeys.push(item))
  tree.value.getCheckedKeys().forEach(item => changedKeys.push(item))
  data.menuIds = changedKeys

  saveRolePermissions(data).then((res) => {
    if (res.code == 200) {
      ElNotification.success({
        title: '通 知',
        message: '绑定成功 !!!',
        duration: 1200,
        showClose: false
      })
      menuDialogVis.value = false
      // 操作管理员角色后需要重新登录
      if (roleFlag.value === 'ADMIN') {
        userStore.resetUser()
      }
    }
    
  }).catch(err => {
    ElNotification.error({
      title: '通 知',
      message: '绑定失败 !!!',
      duration: 1200,
      showClose: false
    })
    console.log(err)
  })
  
}

// 重置搜索
const reset = () => {
  name.value = ""
  loadRoleList()
}

onMounted(() => {
  loadRoleList()
})
</script>


<style scoped>
.ml-5 {
  margin-left: 5px;
}
.headerBg {
  background: #eee!important;
}
</style>
