<template>
    <div class="contentHeight">
        <el-menu default-active="1-4-1" class="global-menu" @open="handleOpen" @close="handleClose"
                 :collapse="isCollapse" @select="handleSelect">
            <el-menu-item index="knowledgeBase">
                <i class="el-icon-menu"></i>
                <span slot="title">知识库</span>
            </el-menu-item>
            <el-menu-item index="2">
                <i class="el-icon-menu"></i>
                <span slot="title">空间整合</span>
            </el-menu-item>
            <el-menu-item index="3">
                <i class="el-icon-setting"></i>
                <span slot="title">用户反馈</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script>
    export default {
        //组件
        components: {},
        // 定义数据
        data() {
            return {
                isCollapse: false
            }
        },

        //监听器
        watch: {},

        created() {

        },

        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            handleSelect(key, keyPath){
                this.$emit('changeCom', key)
            }
        }
    }
</script>

<style scoped type="scss">
    .global-menu:not(.el-menu--collapse) {
        width: 179px;
        height: 100%;
    }

    .contentHeight{
        height: calc(100% - 64px);
        display: inline-block;
        float: left;
    }

    .el-menu-item{
        padding: 0 35px !important;
        text-align: left;
    }
</style>
