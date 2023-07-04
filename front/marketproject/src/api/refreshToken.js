//设置token快过期时间,当前时间+70小时
export const setTokenOverTime = () => {
    let t = new Date().getTime() + 70 * 60 * 60 * 1000
    let d = new Date(t)
    let theMonth = d.getMonth() + 1
    let theDate = d.getDate()
    let theHours = d.getHours()
    let theMinutes = d.getMinutes()
    if (theMonth < 10) {
        theMonth = '0' + theMonth
    }
    if (theDate < 10) {
        theDate = '0' + theDate
    }
    if (theHours < 10) {
        theHours = '0' + theHours
    }
    if (theMinutes < 10) {
        theMinutes = '0' + theMinutes
    }
    let date = d.getFullYear() + '-' + theMonth + '-' + theDate
    let time = theHours + ':' + theMinutes
    let Spare = date + ' ' + time
    return Spare;
}

// 判断token是否即将过期
const isTokenExpired = () => {
    let curTime = new Date();
    //获取即将过期时间
    let setTokenOverTime = localStorage.getItem("BeforeTokenOverTime")
    if (setTokenOverTime) {
        // 判断当前时间是否大于即将过期时间
        if (curTime > new Date(setTokenOverTime)) {
            return true
        }
    }
    return false;
}

let refreshFlag = false

// 刷新token
export const isRefreshToken = (instance, config) => {

    // 判断token是否即将过期，且不是请求刷新token的接口
    if (isTokenExpired() && !refreshFlag) {
        refreshFlag = true
        if (localStorage.getItem('user')) {
            let user = JSON.parse(localStorage.getItem('user'))
            instance.get('/refreshToken/' + user.id).then(res => {
                if (res.data.code === '200') {
                    let ret = res.data.data
                    // 更新 store和缓存里的值
                    localStorage.setItem("token", ret.newToken)
                    // 更新即将过期时间
                    localStorage.setItem("BeforeTokenOverTime", setTokenOverTime())
                } else if (res.data.code === '500') {
                    sessionStorage.clear()
                }
            }).finally(()=>{
                refreshFlag = false
                return config
            })
        }
    } else {
        return config
    }
}

export default setTokenOverTime()