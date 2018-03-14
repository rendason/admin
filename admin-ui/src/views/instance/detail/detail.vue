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
                        title : "实例ID",
                        key : "instanceId",
                        align: 'center'
                    },
                    {
                        title : "账户",
                        key : "accountName",
                        align: 'center'
                    },
                    {
                        title : "地区",
                        key : "regionId",
                        align: 'center'
                    },
                    {
                        title : "实例名称",
                        key : "instanceName",
                        align: 'center'
                    },
                    {
                        title : "公网IP",
                        key : "publicIpAddress",
                        align: 'center',
                        render: (h, params) => {
                            return params.row.publicIpAddress.length > 0 ? params.row.publicIpAddress[0] : "";
                        }
                    },
                    {
                        title : "实例状态",
                        key : "status",
                        align: 'center'
                    },
                    {
                        title : "创建时间",
                        key : "creationTime",
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
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
                                            this.show(params.index)
                                        }
                                    }
                                }, '查看')
                            ]);
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
            show (index) {
                const row = this.tableData[index];
                this.$Modal.info({
                    title: '实例详情',
                    width: 600,
                    render(h) {
                        return [h("br")].concat([
                            [
                                {key: "accountName", title: "账户名"},
                                {key: "instanceName", title: "实例名"}
                            ], [
                                {key: "hostName", title: "主机名"},
                                {key: "instanceId", title: "实例ID"}
                            ], [
                                {key: "regionId", title: "所在地区"},
                                {key: "securityGroupIds", title: "安全组"}
                            ], [
                                {key: "creationTime", title: "创建时间"},
                                {key: "status", title: "运行状态"}
                            ], [
                                {key: "imageId", title: "镜像"},
                                {key: "osname", title: "系统名"}
                            ], [
                                {key: "instanceType", title: "实例规格"},
                                {key: "ioOptimized", title: "IO优化", mapper: (value) => value?"是":"否"}
                            ], [
                                {key: "cpu", title: "CPU核数"},
                                {key: "memory", title: "内存", mapper: (value) => value + "MB"}
                            ], [
                                {key: "instanceChargeType", title: "实例付费方式", mapper: (value) => value === "PrePaid" ? "包年包月" : "按量付费"},
                                {key: "expiredTime", title: "到期时间"}
                            ], [
                                {key: "internetChargeType", title: "网络付费方式", mapper: (value) => value === "PayByTraffic" ? "按流量计费" : "固定宽带"},
                                {key: "instanceNetworkType", title: "实例网络类型", mapper: (value) => value === "classic" ? "经典网络" : "专有网络"}
                            ], [
                                {key: "publicIpAddress", title: "公网IP"},
                                {key: "innerIpAddress", title: "内网IP"}
                            ], [
                                {key: "internetMaxBandwidthIn", title: "公网入带宽", mapper: (value) => value + "Mbps"},
                                {key: "internetMaxBandwidthOut", title: "公网出带宽", mapper: (value) => value + "Mbps"}
                            ]
                        ].map((group) => {
                            const cols = [];
                            group.forEach((value) => {
                                cols.push(h('Col', {
                                    props: {
                                        span: 4
                                    },
                                    style: {
                                        textAlign: 'right',
                                        fontWeight: 700,
                                    }
                                }, value.title));
                                cols.push(h('Col', {
                                    props: {
                                        span: 7,
                                        offset: 1,
                                    },
                                }, value.mapper?value.mapper(row[value.key]):row[value.key]));
                            });
                            return h("p", [h("br"), h('Row', cols)]);
                        }));
                    }
                    //content: Object.keys(row).map(function(field){return field + " : " + row[field]}).join("<br/>")
                })
            },
            getData() {
                return this.$ajax.get("/instances", {
                    params : {
                        projection : "instanceProjection",
                        page : this.pagination.number,
                        size : this.pagination.size
                    }
                }).then((res) => {
                        this.tableData = res.data._embedded.instances;
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