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
                :width="552"
                @on-ok="submitData">
            <Form :model="modal.formData" :label-width="100">
                <FormItem label="名称">
                    <Input v-model="modal.formData.name"></Input>
                </FormItem>
                <FormItem label="监测实例">
                    <Transfer
                            :data="instanceData"
                            :target-keys="modal.formData.instanceIds"
                            :render-format="transferRender"
                            @on-change="changeInstanceIds"></Transfer>
                </FormItem>
                <FormItem label="监测策略">
                    <Select v-model="modal.formData.strategyIds" multiple>
                        <Option v-for="item in strategyData" :value="item.id" :key="item.id">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="达标策略上限">
                    <InputNumber :min="0" v-model="modal.formData.satisfiedStrategiesLimit"></InputNumber>
                </FormItem>
                <FormItem label="动作名称">
                    <Select v-model="modal.formData.actionId">
                        <Option v-for="item in actionData" :value="item.id" :key="item.id">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="状态">
                    <Input v-model="modal.formData.status" disabled></Input>
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
                        title : "监测实例",
                        key : "instanceIds",
                        align: 'center',
                        render : (h, params) => {
                            return params.row.instanceIds.join(",")
                        }
                    },
                    {
                        title : "监测策略",
                        key : "strategyNames",
                        align: 'center',
                        render : (h, params) => {
                            return params.row.strategyNames.join(",")
                        }
                    },
                    {
                        title : "达标策略上限",
                        key : "satisfiedStrategiesLimit",
                        align: 'center'
                    },
                    {
                        title : "动作名称",
                        key : "actionName",
                        align: 'center'
                    },
                    {
                        title : "状态",
                        key : "status",
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            const running = params.row.status === "RUNNING";
                            const url = params.row._links.self.href;
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            running ? this.stop(url) : this.start(url);
                                        }
                                    }
                                }, running ? "停止" : "启动")
                            ]);
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
                        defaultUrl : "/monitors",
                        url : "",
                        name : "",
                        instanceIds : [],
                        strategyIds : [],
                        satisfiedStrategiesLimit : 1,
                        actionId : "",
                        status : "STOPPED"
                    },
                    formData : {},
                    metric : {}
                },
                instanceData : [],
                strategyData : [],
                actionData : []
            }
        },
        created() {
            this.getData();
            this.getInstanceData();
            this.getStrategyData();
            this.getActionData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                this.$ajax.get(this.modal.defaultFormData.defaultUrl,{
                    params : {
                        projection : "monitorProjection",
                        page : pagination.number,
                        size : pagination.size
                    }
                }).then((res) => {
                    this.tableData = res.data._embedded.monitors;
                    this.pagination = res.data.page;
                })
            },
            getInstanceData() {
                this.$ajax.get("/instances", {
                    params : {size : 10000}
                }).then((res) => {
                    this.instanceData = res.data._embedded.instances.map(function(e){
                        return {"key":e["instanceId"], "label":e["instanceName"]}
                    });
                })
            },
            getStrategyData() {
                this.$ajax.get("/strategies", {
                    params : {size : 10000}
                }).then((res) => {
                    this.strategyData = res.data._embedded.strategies.map(function(e){
                        const url = e._links.self.href;
                        e.id = url.substring(url.lastIndexOf("/")+1);
                        return e;
                    });
                });
            },
            getActionData() {
                this.$ajax.get("/actions", {
                    params : {size : 10000}
                }).then((res) => {
                    this.actionData = res.data._embedded.actions.map(function(e){
                        const url = e._links.self.href;
                        e.id = url.substring(url.lastIndexOf("/")+1);
                        return e;
                    });
                });
            },
            transferRender(item) {
                return item.label;
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
            start(url) {
                this.$ajax.post(url + "/start").then((res) => {
                    this.$Message.success("启动成功");
                    this.getData();
                }).catch((e) => {
                    this.$Message.error(e.toString());
                    this.getData();
                });
                this.getData();
            },
            stop(url) {
                this.$ajax.post(url + "/stop").then((res) => {
                    this.$Message.success("停止成功");
                    this.getData();
                }).catch((e) => {
                    this.$Message.error(e.toString());
                    this.getData();
                });
            },
            clearSelected() {
                this.$refs.mainTable.clearCurrentRow();
            },
            changeInstanceIds(newTargetKeys) {
                this.modal.formData.instanceIds = newTargetKeys;
            },
            changeMetric(value) {
                this.modal.metric = (this.metricData.filter((e) => e.id === value))[0];
                if (!this.modal.metric) this.modal.metric = {};
                this.modal.formData.period = this.modal.metric.period;
            },
            submitData() {
                const formData = this.modal.formData;
                const isNew = formData.url === "";
                const http = isNew ? this.$ajax.post : this.$ajax.put;
                http(isNew ? this.modal.defaultFormData.defaultUrl : formData.url, formData,{
                    paramsSerializer: function(params) {
                        return JSON.stringify(params)
                    }
                }).then((res) => {
                    this.modal.visible = false;
                    this.$Message.success(isNew ? "添加成功" : "修改成功");
                    this.getData();
                }).catch((e) => {
                    this.modal.visible = false;
                    this.$Message.error(e.toString());
                });
                this.clearSelected();
            }
        }
    }
</script>