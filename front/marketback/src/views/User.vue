<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="loadUserList">搜索</el-button>
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
      @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column align="center" prop="id" sortable label="ID" width="80"></el-table-column>
      <el-table-column align="center" prop="username" label="名称"></el-table-column>
      <el-table-column align="center" prop="name" label="昵称"></el-table-column>
      <el-table-column align="center" prop="email" label="邮箱"></el-table-column>
      <el-table-column align="center" prop="uid" label="唯一标识"></el-table-column>
      <el-table-column align="center" prop="role" label="用户角色"></el-table-column>
      <el-table-column align="center" width="280">
        <template #header>
          <el-input v-model="search" placeholder="当前页搜索(昵称)" />
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
    
    <!-- 编辑角色信息 -->
    <el-dialog title="用户信息" v-model="dialogFormVisible" width="30%" >
      <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.uid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-dropdown trigger="click" @command="changeRole">
            <span >
              <span>{{ form.role }}</span>
              <el-icon><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item icon="Apple" :command="rolename" v-for="rolename in RoleNameList" :key="rolename.id">
                  {{ rolename }}  
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-form-item>
      </el-form>
      <template #footer>
        <div>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </template>
        
    </el-dialog>

    <!-- 添加角色信息 -->
    <el-dialog title="用户信息" v-model="addFormVisible" width="30%" >
      <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
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
import { getRoleNameList } from '../api/role.js'
import { getAllUsers, updateUser, delUserById, delUserByIds } from '../api/user.js'
import { ref, onMounted, computed } from 'vue'

const dialogFormVisible = ref(false)
const addFormVisible = ref(false)
const name = ref('')
const search = ref('')
const multipleSelection = ref([])
const tableData = ref([])
const RoleNameList = ref([])
const pageIndex = ref(1)
const pageSize = ref(5)
const total = ref(0)

const form = ref({
  username: '',
  name: '',
  email: '',
  address: '',
  uid: '',
  role: ''
})

const filterTableData = computed(() =>
    tableData.value.filter((data) => 
    !search.value.trim() || data.name.toLowerCase().includes(search.value.trim().toLowerCase())
))

const save = () => {
  updateUser(form.value).then(res => {
    if(res.code == '200'){
      loadUserList()
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
  addFormVisible.value = false
}

const handleAdd = () => {
  addFormVisible.value = true
  form.value = {
    username: '',
    name: '',
    email: '',
    address: '',
    uid: '',
    role: ''
  }
}

const handleEdit = (row) => {
  form.value = row
  loadRoleList()
  dialogFormVisible.value = true
}

const del = (id) => {
  delUserById(id).then((res) => {
    console.log(res)
    loadUserList()
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

// 处理表单多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(item => item.id)
  console.log(multipleSelection.value)
}

// 加载所有用户信息
const loadUserList = async () => {
  let ret = await getAllUsers({
    pageSize: pageSize.value,
    pageIndex: pageIndex.value,
    username: name.value.trim()
  })
  tableData.value = ret.data.records
  total.value = ret.data.total
}

const delBatch = () => {
  if (multipleSelection.value.length > 0) {
    delUserByIds(multipleSelection.value).then((res) => {
      console.log(res)
      loadUserList()
    }).catch((err) => {
      ElNotification.error({
        title: '通 知',
        message: '删除出错 !!!',
        duration: 1200,
        showClose: false
      })
      console.log(err)
    })
  }else(
    ElNotification.warning({
      title: '通 知',
      message: '请选择要删除的数据 !!!',
      duration: 1200,
      showClose: false
    })
  )
}

// // 处理分页
const handleSizeChange = (ps) => {
  pageSize.value = ps
  loadUserList()
}

const handleCurrentChange = (pn) => {
  pageIndex.value = pn
  loadUserList()
}

const reset = () => {
  name.value = ""
  loadUserList()
}

// 加载所有角色信息
const loadRoleList = () => {
  getRoleNameList().then(res => {
    RoleNameList.value = res.data
  })
}

const changeRole = (role) =>{
  form.value.role = role
}

onMounted(() => {
  loadUserList()
})
</script>


<style scoped>
.ml-5 {
  margin-left: 5px;
}
</style>
  