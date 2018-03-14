<template>
    <div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px; overflow: hidden">
            <div style="float: right;">
                <Page :total="pagination.totalElements" :current="pagination.number+1" @on-change="changePage"></Page>
            </div>
        </div>
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
                        key : "metric",
                        align: 'center'
                    },
                    {
                        title : "描述",
                        key : "description",
                        align: 'center'
                    },
                    {
                        title : "单位",
                        key : "unit",
                        align: 'center'
                    },
                    {
                        title : "周期",
                        key : "period",
                        align: 'center'
                    },
                    {
                        title : "方法",
                        key : "method",
                        align : 'center',
                        render : (h, params) => {
                            return params.row.method.join(",")
                        }
                    },
                    {
                        title : "支持",
                        key : "support",
                        align: 'center',
                        render : (h, params) => {
                            return h('Icon', {
                                props: {
                                    type: params.row.support ? 'checkmark': "close"
                                }
                            })
                        }
                    }
                ],
                pagination : {
                    totalElements : 0,
                    totalPages : 0,
                    size : 10,
                    number : 0
                }
            }
        },
        created() {
            this.getData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                return this.$ajax.get("/metrics",{
                    params : {
                        page : pagination.number,
                        size : pagination.size
                    }
                })
                    .then((res) => {
                        this.tableData = res.data._embedded.metrics;
                        this.pagination = res.data.page;
                    })
            },
            changePage(index) {
                this.pagination.number = index - 1;
                this.getData();
            }
        }
    }
</script>