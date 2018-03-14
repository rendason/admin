<template>
    <div>
        <Table ref="mainTable" :data="tableData" :columns="tableColumns" stripe highlight-row @on-current-change="selected"></Table>
        <div style="margin: 10px; overflow: hidden">
            <div style="float: right;">
                <Page :total="pagination.totalElements" :current="pagination.number+1" @on-change="changePage"></Page>
            </div>
            <div style="float: left">
                <Button type="primary" @click="add">添加</Button>
                <Button type="primary" @click="edit">修改</Button>
                <Button type="primary" @click="del">删除</Button>
            </div>
        </div>
        <Modal
                v-model="modal.visible"
                :title="modal.title"
                :loading="modal.loading"
                @on-ok="submitData">
            <Form :model="modal.formData" :label-width="100">
                <FormItem label="实例名">
                    <Input v-model="modal.formData.name"></Input>
                </FormItem>
                <FormItem label="账户">
                    <Select v-model="modal.formData.accountId" @on-change="changeAccountOrRegion">
                        <Option v-for="item in accountData" :value="item.id" :key="item.id">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="地区">
                    <Select v-model="modal.formData.regionId" @on-change="changeAccountOrRegion">
                        <Option v-for="item in regionData" :value="item.regionId" :key="item.regionId">{{ item.localName }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="实例名">
                    <Input v-model="modal.formData.instanceName"></Input>
                </FormItem>
                <FormItem label="镜像">
                    <Select v-model="modal.formData.imageId">
                        <Option v-for="item in imageData" :value="item.imageId" :key="item.imageId">{{ item.imageName }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="实例规格">
                    <Select v-model="modal.formData.instanceType">
                        <Option v-for="item in instanceTypeData" :value="item.instanceTypeId" :key="item.instanceTypeId">{{ item.instanceTypeId }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="安全组">
                    <Select v-model="modal.formData.securityGroupId">
                        <Option v-for="item in securityGroupData" :value="item.securityGroupId" :key="item.securityGroupId">{{ item.securityGroupName }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="密码">
                    <Input type="password" v-model="modal.formData.password"></Input>
                </FormItem>
                <FormItem label="公网出宽带">
                    <InputNumber :min="1"  :max="100" v-model="modal.formData.internetMaxBandwidthOut"></InputNumber>
                </FormItem>
            </Form>
        </Modal>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                tableData : [],
                tableColumns : [
                    {
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title : "名称",
                        key : "name",
                        align: 'center'
                    },
                    {
                        title : "账户",
                        key : "accountName",
                        align: 'center'
                    },
                    {
                        title : "实例名",
                        key : "instanceName",
                        align: 'center'
                    },
                    {
                        title : "地区",
                        key : "regionId",
                        align: 'center'
                    },
                    {
                        title : "镜像",
                        key : "imageId",
                        align: 'center'
                    },
                    {
                        title : "实例规格",
                        key : "instanceType",
                        align: 'center'
                    },
                    {
                        title : "安全组",
                        key : "securityGroupId",
                        align: 'center'
                    },
                    {
                        title : "公网出带宽",
                        key : "internetMaxBandwidthOut",
                        align: 'center',
                        render(h, params) {
                            return params.row.internetMaxBandwidthOut + "Mbps";
                        }
                    }
                ],
                pagination : {
                    totalElements : 0,
                    totalPages : 0,
                    size : 10,
                    number : 0
                },
                modal : {
                    visible : false,
                    loading : true,
                    title : "",
                    defaultFormData : {
                        defaultUrl : "/instanceTemplates",
                        url : "",
                        accountId : "",
                        regionId : "",
                        imageId : "",
                        instanceType : "",
                        securityGroupId : "",
                        instanceName : "",
                        password : "",
                        internetMaxBandwidthOut : 1,
                        autoCreate : false
                    },
                    formData : {}
                },
                accountData : [],
                regionData : [],
                imageData : [],
                instanceTypeData : [],
                securityGroupData : []
            }
        },
        created() {
            this.getData();
            this.getAccountData();
            this.getRegionData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                this.$ajax.get(this.modal.defaultFormData.defaultUrl,{
                    params : {
                        projection : "instanceTemplateProjection",
                        page : pagination.number,
                        size : pagination.size
                    }
                }).then((res) => {
                    this.tableData = res.data._embedded.instanceTemplates;
                    this.pagination = res.data.page;
                })
            },
            getAccountData() {
                this.$ajax.get("/accounts", {
                    params : {size : 10000}
                }).then((res) => {
                    this.accountData = res.data._embedded.accounts.map(function(e){
                        const url = e._links.self.href;
                        e.id = url.substring(url.lastIndexOf("/")+1);
                        return e;
                    });
                });
            },
            getRegionData() {
                this.$ajax.get("/regions", {
                    params : {size : 10000}
                }).then((res) => {
                    this.regionData = res.data._embedded.regions;
                });
            },
            getImageData() {
                this.$ajax.get("/images", {
                    params : {
                        accountId : this.modal.formData.accountId,
                        regionId : this.modal.formData.regionId
                    }
                }).then((res) => {
                        this.imageData = Object.keys(res.data).length === 0 ? [] : res.data._embedded.images;
                });
            },
            getInstanceTypeData() {
                this.$ajax.get("/instanceTypes", {
                    params : {
                        accountId : this.modal.formData.accountId,
                        regionId : this.modal.formData.regionId
                    }
                }).then((res) => {
                    this.instanceTypeData = Object.keys(res.data).length === 0 ? [] : res.data._embedded.instanceTypes;
                });
            },
            getSecurityGroupData() {
                this.$ajax.get("/securityGroups", {
                    params : {
                        accountId : this.modal.formData.accountId,
                        regionId : this.modal.formData.regionId
                    }
                }).then((res) => {
                    this.securityGroupData = Object.keys(res.data).length === 0 ? [] : res.data._embedded.securityGroups;
                });
            },
            changePage(index) {
                this.pagination.number = index - 1;
                this.getData();
            },
            selected(current, last) {
                if (current === null) {
                    this.modal.formData = this.modal.defaultFormData;
                } else {
                    this.modal.formData = current;
                    current.url = current._links.self.href;
                }
            },
            add() {
                this.clearSelected();
                this.modal.visible = true;
                this.modal.title = "添加";
            },
            edit() {
                if (this.modal.formData.url === "") {
                    this.$Message.warning('请选择一条记录');
                } else {
                    this.modal.visible = true;
                    this.modal.title = "编辑";
                }
            },
            del() {
                const url = this.modal.formData.url;
                if (url === "") {
                    this.$Message.warning('请选择一条记录');
                } else {
                    this.$ajax.delete(url).then(res => {
                        this.clearSelected();
                        this.$Message.success("删除成功");
                        this.getData();
                    }).catch(e => {
                        this.$Message.error(e.toString());
                    })
                }
            },
            changeAccountOrRegion(value) {
                if (this.modal.formData.accountId !== "" && this.modal.formData.regionId !== "") {
                    this.getImageData();
                    this.getInstanceTypeData();
                    this.getSecurityGroupData();
                }
            },
            clearSelected() {
                this.$refs.mainTable.clearCurrentRow();
            },
            submitData() {
                const formData = this.modal.formData;
                const isNew = formData.url === "";
                const http = isNew ? this.$ajax.post : this.$ajax.put;
                http(isNew ? this.modal.defaultFormData.defaultUrl : formData.url, formData,{
                    paramsSerializer: function(params) {
                        return JSON.stringify(params)
                    }
                }).then(res => {
                    this.modal.visible = false;
                    this.$Message.success(isNew ? "添加成功" : "修改成功");
                    this.getData();
                }).catch(e => {
                    this.modal.visible = false;
                    this.$Message.error(e.toString());
                })
                this.clearSelected();
            }
        }
    }
</script>