<style lang="less">
    @import "./home.less";
</style>
<template>
    <div class="home-main">
        <Row style="height:200px">
            <Col span="12">
                <Card>
                    <Row>
                        <Col span="12">
                            <Row align="middle">
                                <div>
                                    <b class="card-user-infor-name">Admin</b>
                                    <p>super admin</p>
                                </div>
                            </Row>
                        </Col>
                        <Col span="12">
                            <Row style="height:100px">
                                <Col span="8"><p class="notwrap">上次登录时间:</p></Col>
                                <Col span="16" class="padding-left-8">2017.09.12-13:32:20</Col>
                            </Row>
                            <Row style="height:100px">
                                <Col span="8"><p class="notwrap">上次登录地点:</p></Col>
                                <Col span="16" class="padding-left-8">北京</Col>
                            </Row>
                        </Col>
                    </Row>

                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>
export default {
    name: 'home',
    data () {
        return {
            //
        };
    },
    created() {
        this.getDashboard();
    },
    methods : {
        getDashboard() {
            this.$ajax.get("/dashboard").then((res) => {
                console.log(res.data)
            })
        }
    }
};
</script>
