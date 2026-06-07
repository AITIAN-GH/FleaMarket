<template>
  <div>
    <div style="margin: 10px 0">
      <el-input
        style="width: 200px"
        placeholder="请输入名称"
        suffix-icon="search"
        v-model="name"
      ></el-input>
      <el-button class="ml-5" type="primary" @click="loadGoodsList"
        >搜索</el-button
      >
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button
        type="primary"
        @click="handleAdd"
        v-if="userAuths.includes('pro.add')"
        >新增 <i class="el-icon-circle-plus-outline"></i
      ></el-button>
      <el-popconfirm
        confirm-button-text="确定"
        cancel-button-text="取消"
        icon-color="#626AEF"
        width="156px"
        title="删除这些商品？"
        @confirm="delBatch"
        v-if="userAuths.includes('pro.deleteBatch')"
      >
        <template #reference>
          <el-button type="danger">删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <el-table
      :default-sort="{ prop: 'id', order: 'descending' }"
      :data="filterTableData"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="id"
        label="ID"
        sortable
        width="80"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="productName"
        label="名称"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="productAuthor"
        label="作者"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="proPrice"
        label="原价"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="delPrice"
        label="现价"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="stock"
        label="库存"
        width="230"
      ></el-table-column>
      <el-table-column align="center" label="封面">
        <template #default="scope">
          <img
            style="max-height: 70px; max-width: 100px"
            :src="baseURL + scope.row.classImg"
          />
        </template>
      </el-table-column>
      <el-table-column prop="classify" label="类别"></el-table-column>
      <el-table-column width="280" align="center">
        <template #header>
          <el-input v-model="search" placeholder="当前页搜索(名称)" />
        </template>
        <template #default="scope">
          <el-button type="success" @click="gotoComment(scope.row)"
            >看评论<i class="el-icon-edit"></i
          ></el-button>
          <el-button
            type="primary"
            @click="handleEdit(scope.row)"
            v-if="userAuths.includes('pro.edit')"
            >编辑 <i class="el-icon-edit"></i
          ></el-button>
          <el-popconfirm
            confirm-button-text="确定"
            cancel-button-text="取消"
            icon-color="#626AEF"
            title="确定删除？"
            @confirm="del(scope.row.proId)"
            v-if="userAuths.includes('pro.delete')"
          >
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
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog
      title="商品信息"
      v-model="dialogFormVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        label-width="80px"
        :model="form"
        :rules="rules"
        ref="ruleFormRef"
      >
        <el-form-item
          label="商品名称"
          prop="productName"
          :rules="[
            { required: true, validator: validateProductName, trigger: 'blur' },
          ]"
        >
          <el-input v-model="form.productName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="商品作者"
          prop="productAuthor"
          :rules="[
            {
              required: true,
              validator: validateProductAuthor,
              trigger: 'blur',
            },
          ]"
        >
          <el-input v-model="form.productAuthor" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="商品原价"
          prop="proPrice"
          :rules="[
            { required: true, validator: validateProPrice, trigger: 'blur' },
          ]"
        >
          <el-input v-model="form.proPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="商品现价"
          prop="delPrice"
          :rules="[
            { required: true, validator: validateDelPrice, trigger: 'blur' },
          ]"
        >
          <el-input v-model="form.delPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="商品库存"
          prop="stock"
          :rules="[
            { required: true, validator: validateStock, trigger: 'blur' },
          ]"
        >
          <el-input v-model="form.stock" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="商品封面">
          <el-input
            hidden
            v-model="form.classImg"
            autocomplete="off"
          ></el-input>
          <el-upload
            action="/api/file/upload/proImg"
            class="avatar-uploader"
            ref="uploadProImgRef"
            :show-file-list="false"
            :on-success="handleSuccess"
            :before-upload="beforeProImgUpload"
          >
            <component
              :is="'UploadFilled'"
              v-if="!imageUrl"
              style="width: 100px; height: 100px"
            ></component>
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <template #tip>
              <div class="el-upload__tip text-red">一次只能上传一张封面图</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品类别">
          <el-dropdown trigger="click" @command="changeClassify">
            <span>
              <span>{{ form.classifyName }}</span>
              <el-icon><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  icon="Apple"
                  :command="classify"
                  v-for="classify in classifyList"
                  :key="classify.id"
                >
                  {{ classify.classifyName }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-form-item>
      </el-form>
      <template #footer>
        <div>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save(ruleFormRef)">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
  
<script setup>
import { baseURL } from "../config";
import { ElNotification } from "element-plus";
import {
  getAllGoods,
  updateUserProduct,
  delProductByIds,
  getClassifyList,
} from "../api/goods.js";
import { ref, onMounted, computed, reactive } from "vue";
import { userInfo } from "../store";
import router from "../router/index.js";

const userStore = userInfo();
const name = ref("");
const search = ref("");
const total = ref(0);
const pageSize = ref(5);
const pageIndex = ref(1);
const userAuths = ref([]);
const tableData = ref([]);
const classifyList = ref([]);
const dialogFormVisible = ref(false);
const multipleSelection = ref([]);
const ruleFormRef = ref(null);
const uploadProImgRef = ref(null);
const imageUrl = ref("");

const form = reactive({
  id: 0,
  productName: "",
  productAuthor: "",
  proPrice: 0,
  proId: 0,
  delPrice: 0,
  classImg: "",
  classid: 0,
  stock: 0,
  classifyName: "",
});

// 图片上传处理
const beforeProImgUpload = (rawFile) => {
  // 将选择的文件保存到 formData 中
  //   if (rawFile.type !== 'img/jpg') {
  //     ElMessage.error('Avatar picture must be JPG format!')
  //     return false
  //   }
  return true;
};

const handleSuccess = (response, uploadFile) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw);
  form.classImg = response.data;
};

const handleClose = (done) => {
  uploadProImgRef.value.clearFiles();
  imageUrl.value = "";
  // 重置表单字段
  ruleFormRef.value.resetFields();
  // 重置表单数据
  form.id = 0;
  form.productName = "";
  form.productAuthor = "";
  form.proPrice = 0;
  form.delPrice = 0;
  form.classImg = "";
  form.classid = 0;
  form.stock = 1;
  form.classifyName = "";
  done();
};

const filterTableData = computed(() =>
  tableData.value.filter(
    (data) =>
      !search.value.trim() ||
      data.productName.toLowerCase().includes(search.value.trim().toLowerCase())
  )
);

const getClassfyBid = computed(() =>
  classifyList.value.filter(
    (data) => !form.classifyName || data.classifyName == form.classifyName
  )
);

// 查看特定商品评论
const gotoComment = (row) => {
  // 路由跳转时携带上productId参数
  router.push(
    "/comment?productId=" + row.proId + "&productName=" + row.productName
  );
};

const validateProductName = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请输入产品名 !!"));
  } else {
    callback();
  }
};

const validateProductAuthor = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请输入生产者名或厂家 !!"));
  } else {
    callback();
  }
};

const validateProPrice = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请输入原价 !!"));
  } else if (isNaN(value)) {
    callback(new Error("请输入数字 !!"));
  } else if (value > 5000) {
    callback(new Error("请良心定价 !!"));
  } else {
    callback();
  }
};

const validateDelPrice = (rule, value, callback) => {
  if (form.proPrice == 0 && value != 0) {
    callback(new Error("请输入原价 !!"));
  } else if (value == 0) {
    callback(new Error("请输入现价 !!"));
  } else if (isNaN(value)) {
    callback(new Error("请输入数字 !!"));
  } else if (value > 5000) {
    callback(new Error("请良心定价 !!"));
  } else if (parseInt(value) > form.proPrice) {
    callback(new Error("现价不能高于原价 !!"));
  } else {
    callback();
  }
};

const validateStock = (rule, value, callback) => {
  if (!Number(value) && value != 0){
    callback(new Error("输入必须是数字 !!"));
  } else if (value < 0 || value > 99) {
    callback(new Error("库存数必须是0 ~ 99 !"));
  } else {
    callback();
  }
};

const rules = reactive({
  productName: [{ validator: validateProductName, trigger: "blur" }],
  productAuthor: [{ validator: validateProductAuthor, trigger: "blur" }],
  proPrice: [{ validator: validateProPrice, trigger: "blur" }],
  delPrice: [{ validator: validateDelPrice, trigger: "blur" }],
  stock: [{ validator: validateStock, trigger: "blur" }],
});

const save = (formEl) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      updateUserProduct({
        product: { ...form },
        userId: userStore.user.id,
        proId: form.proId,
      })
        .then((res) => {
          if (res.code == "200") {
            ElNotification.warning({
              title: "通知",
              message: ">_< 操作成功  !!!",
              duration: 1200,
              showClose: false,
            });
            loadGoodsList();
          } else {
            ElNotification.warning({
              title: "通知",
              message: ">_< 添加失败! 格式不对  !!!",
              duration: 1200,
              showClose: false,
            });
          }
        })
        .catch((err) => {
          ElNotification.error({
            title: "通 知",
            message: "更新失败 !!!",
            duration: 1200,
            showClose: false,
          });
          console.log(err);
        });
    } else {
      ElNotification.warning({
        title: "通知",
        message: ">_< 添加失败! 格式不对  !!!",
        duration: 1200,
        showClose: false,
      });
    }
    resetForm();
    dialogFormVisible.value = false;
  });
};

const handleAdd = () => {
  loadClassifyList();
  form.value = {
    id: 0,
    productName: "",
    productAuthor: "",
    proPrice: "",
    proId: 0,
    delPrice: 0,
    classImg: "",
    classid: 0,
    stock: 1,
    classifyName: "",
  };
  form.classid = classifyList.value[0].bid;
  form.classifyName = classifyList.value[0].classifyName;
  dialogFormVisible.value = true;
};

const handleEdit = (row) => {
  loadClassifyList();
  (form.id = row.id),
    (form.productName = row.productName),
    (form.productAuthor = row.productAuthor),
    (form.proPrice = row.proPrice),
    (form.proId = row.proId),
    (form.delPrice = row.delPrice),
    (form.classImg = row.classImg),
    (form.classifyName = row.classify);
  form.stock = row.stock;
  form.classid = getClassfyBid.value[0].bid;
  if (row.classImg != "") {
    imageUrl.value = "/api" + row.classImg;
  }
  // 遍历数组getClassfyBid中的元素
  dialogFormVisible.value = true;
};

const del = (id) => {
  delProductByIds({
    userId: userStore.user.id,
    delIds: [id],
  })
    .then((res) => {
      if (res.code == "200") {
        loadGoodsList();
      } else {
        ElNotification.warning({
          title: "通 知",
          message: "删除失败 !!!",
          duration: 1200,
          showClose: false,
        });
      }
    })
    .catch((err) => {
      ElNotification.error({
        title: "通 知",
        message: "删除出错 !!!",
        duration: 1200,
        showClose: false,
      });
      console.log(err);
    });
};

// 处理表单多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map((item) => item.proId);
};

// 加载用户商品信息
const loadGoodsList = () => {
  getAllGoods({
    id: userStore.user.id,
    name: name.value,
    pageIndex: pageIndex.value,
    pageSize: pageSize.value,
  })
    .then((ret) => {
      tableData.value = ret.data.tableData;
      total.value = ret.data.total;
    })
    .catch((err) => {
      console.log(err);
    });
};

const delBatch = () => {
  if (multipleSelection.value.length > 0) {
    delProductByIds({
      userId: userStore.user.id,
      delIds: multipleSelection.value,
    })
      .then((res) => {
        if (res.code == "200") {
          loadGoodsList();
        } else {
          ElNotification.warning({
            title: "通 知",
            message: "删除失败 !!!",
            duration: 1200,
            showClose: false,
          });
        }
      })
      .catch((err) => {
        ElNotification.error({
          title: "通 知",
          message: "删除出错 !!!",
          duration: 1200,
          showClose: false,
        });
        console.log(err);
      });
  } else
    ElNotification.warning({
      title: "通 知",
      message: "请选择要删除的数据 !!!",
      duration: 1200,
      showClose: false,
    });
};

// // 处理分页
const handleSizeChange = (ps) => {
  pageSize.value = ps;
  loadGoodsList();
};

const handleCurrentChange = (pn) => {
  pageIndex.value = pn;
  loadGoodsList();
};

const reset = () => {
  name.value = "";
  loadGoodsList();
};

const resetForm = () => {
  uploadProImgRef.value.clearFiles();
  imageUrl.value = "";
  ruleFormRef.value.resetFields();
  // 重置表单数据
  form.id = 0;
  form.productName = "";
  form.productAuthor = "";
  form.proPrice = 0;
  form.delPrice = 0;
  form.classImg = "";
  form.classid = 0;
  form.stock = 1;
  form.classifyName = "";
};

// 更改商品分类
const changeClassify = (classfiy) => {
  form.classid = classfiy.bid;
  form.classifyName = classfiy.classifyName;
};

// 加载所有分类信息
const loadClassifyList = () => {
  getClassifyList().then((res) => {
    classifyList.value = res.data;
  });
};

onMounted(() => {
  loadGoodsList(), loadClassifyList(), (userAuths.value = userStore.user.auths);
});
</script> 
  
<style lang="scss" scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.ml-5 {
  margin-left: 5px;
}
</style>
    