<template>
    <div>
        <div id="main"></div>
    </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { onMounted, reactive } from "vue"
import { getchartData } from '../api/ECharts.js'

let mainContainer
const chartdata = reactive([])

const initContainer = (chartdata) => {
    
    //设置div容器高宽
    resizeMainContainer();
    // 初始化图表
    let mainChart = echarts.init(mainContainer)

    window.addEventListener('resize', () => {
        resizeMainContainer();
        mainChart.resize();
    })
    
    // 第五步 使用刚指定的配置项和数据显示图表。
    mainChart.setOption({
        title: {
            text: '商品概览',
            left: 'center',
            top: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        stillShowZeroSum: false,
        // 图例
        legend: {
            orient: 'vertical',
            x: 'right',
            top: '10%',
        },
        series: [{
            name: '分类',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            data: chartdata
        }]
    })
}

const resizeMainContainer = () => {
    // 获取当前窗口的宽度和高度
    mainContainer.style.width = window.innerWidth*0.28 +'px';
    mainContainer.style.height = window.innerHeight*0.30+'px';
}

const loadData = () => { 
    getchartData().then(res => {
        res.data.forEach(element => {
            let tmp = {name: '',value: 0}
            tmp.name = element.classname
            tmp.value = element.classcount
            chartdata.push(tmp)
        })
        initContainer(chartdata)
    }).catch(err => {
        console.log(err)
    })
}

onMounted (()=> {
    loadData()
    mainContainer = document.getElementById('main')
})
</script>

<style>

</style>