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
                <FormItem label="名称">
                    <Input v-model="modal.formData.name"></Input>
                </FormItem>
                <FormItem label="类型">
                    <Input v-model="modal.formData.type" disabled></Input>
                </FormItem>
                <FormItem label="可释放实例">
                    <Select v-model="modal.formData.instanceIds" multiple>
                        <Option v-for="item in instanceData" :value="item.instanceId" :key="item.instanceId">{{ item.instanceName }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="自动释放">
                    <i-switch size="large" v-model="modal.formData.autoRelease">
                        <span slot="open">开启</span>
                        <span slot="close">关闭</span>
                    </i-switch>
                </FormItem>
                <FormItem label="触发间隔(ms)">
                    <InputNumber :min="0" v-model="modal.formData.triggerInterval"></InputNumber>
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
                        title : "类型",
                        key : "type",
                        align: 'center'
                    },
                    {
                        title : "可释放实例",
                        key : "configs",
                        align: 'center',
                        render(h, params) {
                            const instanceIds = params.row.configs.instanceIds;
                            return instanceIds ? instanceIds.join(",") : instanceIds;
                        }
                    },
                    {
                        title : "自动释放",
                        key : "autoRelease",
                        align: 'center',
                        render : (h, params) => {
                            return h('Icon', {
                                props: {
                                    type: params.row.configs.autoRelease ? 'checkmark': "close"
                                }
                            })
                        }
                    },
                    {
                        title : "触发间隔(ms)",
                        key : "triggerInterval",
                        align: 'center'
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
                        defaultUrl : "/actions",
                        url : "",
                        name : "",
                        type : "SHRINK",
                        instanceIds : [],
                        autoRelease : false,
                        triggerInterval : 0
                    },
                    formData : {}
                },
                instanceData : []
            }
        },
        created() {
            this.getData();
            this.getInstanceData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                return this.$ajax.get("/actions/search/type",{
                    params : {
                        type : "SHRINK",
                        page : pagination.number,
                        size : pagination.size
                    }
                }).then((res) => {
                    this.tableData = res.data._embedded.actions;
                    this.pagination = res.data.page;
                })
            },
            getInstanceData() {
                this.$ajax.get("/instances/search/by-instance-charge-type", {
                    params : {
                        instanceChargeType : "PostPaid",
                        size : 10000
                    }
                }).then((res) => {
                    this.instanceData = res.data._embedded.instances.map(function(e){
                        const url = e._links.self.href;
                        e.id = url.substring(url.lastIndexOf("/")+1);
                        return e;
                    });
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
                    this.modal.formData = {
                        url : current._links.self.href,
                        name : current.name,
                        type : current.type,
                        instanceIds : current.configs.instanceIds,
                        autoRelease : current.configs.autoRelease,
                        triggerInterval : current.triggerInterval
                    }
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
                        this.getData()
                    }).catch(e => {
                        this.$Message.error(e.toString());
                    })
                }
            },
            clearSelected() {
                this.$refs.mainTable.clearCurrentRow();
            },
            submitData() {
                const formData = this.modal.formData;
                const isNew = formData.url === "";
                const http = isNew ? this.$ajax.post : this.$ajax.put;
                http(isNew ? this.modal.defaultFormData.defaultUrl : formData.url, {
                    name : formData.name,
                    type : formData.type,
                    configs : {
                        instanceIds : formData.instanceIds,
                        autoRelease : formData.autoRelease
                    },
                    triggerInterval : formData.triggerInterval
                },{
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