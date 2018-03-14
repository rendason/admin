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
            <Form :model="modal.formData" :label-width="80">
                <FormItem label="名称">
                    <Input v-model="modal.formData.name"></Input>
                </FormItem>
                <FormItem label="AccessKey">
                    <Input v-model="modal.formData.accessKey"></Input>
                </FormItem>
                <FormItem label="AccessSecret">
                    <Input v-model="modal.formData.accessSecret"></Input>
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
                        title : "AccessKey",
                        key : "accessKey",
                        align: 'center'
                    },
                    {
                        title : "AccessSecret",
                        key : "accessSecret",
                        align: 'center',
                        render : (h, params) => {
                            return "****************";
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
                        defaultUrl : "/accounts",
                        url : "",
                        name : "",
                        accessKey : "",
                        accessSecret : ""
                    },
                    formData : {}
                },
            }
        },
        created() {
            this.getData();
        },
        methods : {
            getData() {
                const pagination = this.pagination ? this.pagination : {number: 0, size: 10};
                return this.$ajax.get(this.modal.defaultFormData.defaultUrl,{
                    params : {
                        page : pagination.number,
                        size : pagination.size
                    }
                }).then((res) => {
                    this.tableData = res.data._embedded.accounts;
                    this.pagination = res.data.page;
                })
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