<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="用户代码" >
        <el-input v-model="dataForm.userCd" placeholder="用户代码"></el-input>
      </el-form-item>
      <el-form-item label="用户中文名" >
        <el-input v-model="dataForm.cnName" placeholder="用户中文名"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" >
        <el-input v-model="dataForm.phoneNum" placeholder="联系方式"></el-input>
      </el-form-item>
      <el-form-item label="用户类型" size="mini" prop="status">
        <el-radio-group v-model="dataForm.userType">
          <el-radio :label="1">管理</el-radio>
          <el-radio :label="0">普通</el-radio>
        </el-radio-group>
      </el-form-item>
<!--      <el-form-item label="行政区划标识" >-->
<!--        <el-input v-model="dataForm.adminUnitId" placeholder="行政区划标识"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="行政区划">
        <el-cascader placeholder="点击选择行政区划" class="unit-select"
                v-model="dataForm.adminUnitIds"
                change-on-select
                :props="{ checkStrictly: true }"
                :options="orgList" style="width: 100%"></el-cascader>
      </el-form-item>
      <el-form-item label="用户职务" >
        <el-input v-model="dataForm.userTitle" placeholder="用户职务"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        visible: false,
        roleList: [],
        dataForm: {
            userCd: '',
            cnName: '',
            phoneNum: '',
            userType: '',
            adminUnitId: '',
            adminUnitIds:[],
            adminUnitName:'',
            userTitle: '',
        },
        adminUnits:[],
        dataRule: {
            cnName: [
              { required: true, message: '用户名不能为空', trigger: 'blur' }
            ],
            userCd: [
                { required: true, message: '用户代码', trigger: 'blur' }
            ],
            userType: [
                { required: true, message: '用户类型', trigger: 'blur' }
            ],
        },
        orgList:[],
        orgData:[]
      }
    },
    methods: {
      init (id) {
          this.visible=true
          this.dataForm.id = id || 0
          if (this.dataForm.id) {
              this.$http({
                  url: this.$http.adornUrl(`/sys/user/info/${this.dataForm.id}`),
                  method: 'get',
                  params: this.$http.adornParams()
              }).then(({data}) => {
                  if (data && data.code === 0) {
                      this.dataForm.userCd = data.user.userCd
                      this.dataForm.cnName = data.user.cnName
                      this.dataForm.phoneNum = data.user.phoneNum
                      this.dataForm.userType = data.user.userType
                      this.dataForm.adminUnitId = data.user.adminUnitId
                      this.dataForm.userTitle = data.user.userTitle
                  }
                  this.getOrgList()
              })
          }

      },
      // 表单提交
      dataFormSubmit () {
        this.dataForm.adminUnitId = this.getOrgValue(this.dataForm.adminUnitIds)
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/user/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'userId': this.dataForm.id || undefined,
                'userCd': this.dataForm.userCd,
                'cnName': this.dataForm.cnName,
                'phoneNum': this.dataForm.phoneNum,
                'userType': this.dataForm.userType,
                'adminUnitId': this.dataForm.adminUnitId,
                'userTitle': this.dataForm.userTitle,
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      getOrgList(){
        this.$http({
          url: this.$http.adornUrl('/admin/list'),
          method: 'get',
          params: this.$http.adornParams({
            page:1,
            limit:500
          })
        }).then(({data}) => {
          if(data&&data.page&&data.page.list){
            this.orgList = this.treeDataTranslate(data.page.list, 'adminUnitId')
            this.orgData = data.page.list
          }

        })
      },
      treeDataTranslate (data, id = 'adminUnitId', pid = 'superAdminUnit') {
        this.dataForm.adminUnitIds = this.loadOrgValue(data, this.dataForm.adminUnitId)//加载
        var res = []
        var temp = {}
        for (var i = 0; i < data.length; i++) {
          data[i].value = data[i][id].toString()
          data[i].label = data[i].adminUnitName
          temp[data[i][id]] = data[i]
        }
        for (var k = 0; k < data.length; k++) {
          if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
            if (!temp[data[k][pid]]['children']) {
              temp[data[k][pid]]['children'] = []
            }
            if (!temp[data[k][pid]]['_level']) {
              temp[data[k][pid]]['_level'] = 1
            }
            data[k]['_level'] = temp[data[k][pid]]._level + 1
            temp[data[k][pid]]['children'].push(data[k])
          } else {
            res.push(data[k])
          }
        }
        for(var j=0;j<res.length;j++){
          if(res[j][id]=='387'){
            res.splice(j, 1);
            break;
          }
        }
        return res
      },
      loadOrgValue(list, id){
        if(!id) return [];
        let arr = [], cid = id, x = 5
        while(x>0){
          for(let i=0;i<list.length;i++){
            let n = list[i];
            if(n.adminUnitId==cid){
              arr.push(n.adminUnitId.toString())
              cid = n.superAdminUnit
              break
            }
          }
          x--
        }
        return arr.reverse()
      },
      getOrgValue(ids){
        if(ids==null||ids.length==0) return ''
        return ids[ids.length - 1].toString()
      }
    }
  }
</script>
<style lang="scss">
  .el-cascader-menu{
    overflow:hidden;
  }
  .el-cascader-menu__list{
    padding-left:10px;
  }
  .el-cascader-menu__list li{
    list-style-type: none;
  }
  .el-scrollbar__wrap{
    overflow: auto;
  }
</style>
