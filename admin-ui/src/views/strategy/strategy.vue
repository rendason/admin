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
                <FormItem label="监测项">
                    <Select v-model="modal.formData.metricId" @on-change="changeMetric">
                        <Option v-for="item in metricData" :value="item.id" :key="item.id">
                            <Tooltip :content="item.description">{{ item.metric }}</Tooltip>
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="监测周期(s)">
                    <InputNumber :min="modal.metric.period" :step="modal.metric.period"  v-model="modal.formData.monitorPeriod"></InputNumber>
                </FormItem>
                <FormItem label="周期数量">
                    <InputNumber :min="1"  v-model="modal.formData.periodCount"></InputNumber>
                </FormItem>
                <FormItem label="统计方法">
                    <Select v-model="modal.formData.monitorMethod">
                        <Option v-for="item in modal.metric.method" :value="item" :key="item">{{ item}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="监测项阈值(%)">
                    <InputNumber :min="0" :max="100" v-model="modal.formData.metricThreshold"></InputNumber>
                </FormItem>
                <FormItem label="阈值比较方法">
                    <Select v-model="modal.formData.metricComparison">
                        <Option v-for="item in comparisonData" :value="item.key" :key="item.key">{{ item.value }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="达标周期上限">
                    <InputNumber :min="1" :max="modal.formData.periodCount" v-model="modal.formData.satisfiedPeriodsLimit"></InputNumber>
                </FormItem>
                <FormItem label="达标实例数量">
                    <InputNumber :min="1"  v-model="modal.formData.satisfiedInstancesLimit"></InputNumber>
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
                        title : "监测项",
                        key : "metricName",
                        align: 'center'
                    },
                    {
                        title : "监测周期(s)",
                        key : "monitorPeriod",
                        align: 'center'
                    },
                    {
                        title : "周期数量",
                        key : "periodCount",
                        align: 'center'
                    },
                    {
                        title : "监测方法",
                        key : "monitorMethod",
                        align: 'center'
                    },{
                        title : "监测项阈值(%)",
                        key : "metricThreshold",
                        align: 'center'
                    },

                    {
                        title : "阈值比较方法",
                        key : "comparisonOperator",
                        align: 'center'
                    },
                    {
                        title : "达标周期上限",
                        key : "satisfiedPeriodsLimit",
                        align: 'center'
                    },
                    {
                        title : "达标实例上限",
                        key : "satisfiedInstancesLimit",
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
                        defaultUrl : "/strategies",
                        url : "",
                        name : "",
                        metricId : "",
                        monitorPeriod : 60,
                        periodCount : 0,
                        monitorMethod : "",
                        metricThreshold : 0,
                        metricComparison : "",
                        satisfiedPeriodsLimit : 1,
                        satisfiedInstancesLimit : 1
                    },
                    formData : {},
                    metric : {}
                },
                metricData : [],
                comparisonData : []
            }
        },
        created() {
            this.getData();
            this.getMetricData();
            this.getComparisonData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                this.$ajax.get(this.modal.defaultFormData.defaultUrl,{
                    params : {
                        projection : "strategyProjection",
                        page : pagination.number,
                        size : pagination.size
                    }
                }).then((res) => {
                    this.tableData = res.data._embedded.strategies;
                    this.pagination = res.data.page;
                })
            },
            getMetricData() {
                this.$ajax.get("/metrics", {
                    params : {size : 10000}
                }).then((res) => {
                    this.metricData = res.data._embedded.metrics.map(function(e){
                        const url = e._links.self.href;
                        e.id = url.substring(url.lastIndexOf("/")+1);
                        return e;
                    });
                });
            },
            getComparisonData() {
                this.$ajax.get("/comparisons").then((res) => {
                    this.comparisonData = res.data._embedded.comparisons.map(function(e){
                        return {key : e["function"], value : e["operator"]};
                    })
                })
            },
            changePage(index) {
                this.pagination.number = index - 1;
                this.getData();
            },
            changeMetric(value) {
                this.modal.metric = (this.metricData.filter((e) => e.id === value))[0];
                if (!this.modal.metric) this.modal.metric = {};
                this.modal.formData.monitorPeriod = this.modal.metric.period;
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