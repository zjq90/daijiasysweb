<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <view class="logo-icon">🚗</view>
        <text class="logo-title">代驾系统</text>
      </view>
      <text class="login-subtitle">安全、便捷、专业的代驾服务</text>
    </view>

    <view class="login-form">
      <view class="form-item">
        <text class="form-label">手机号</text>
        <input 
          class="form-input" 
          type="number" 
          placeholder="请输入手机号" 
          v-model="phone"
          maxlength="11"
        />
      </view>

      <view class="form-item" v-if="loginType === 'password'">
        <text class="form-label">密码</text>
        <input 
          class="form-input" 
          :password="!showPassword" 
          placeholder="请输入密码" 
          v-model="password"
        />
        <view class="eye-icon" @click="showPassword = !showPassword">
          {{ showPassword ? '🙈' : '👁️' }}
        </view>
      </view>

      <view class="form-item" v-if="loginType === 'code'">
        <text class="form-label">验证码</text>
        <input 
          class="form-input code-input" 
          type="number" 
          placeholder="请输入验证码" 
          v-model="code"
          maxlength="6"
        />
        <view class="code-btn" :class="{ disabled: countdown > 0 }" @click="sendCode">
          {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
        </view>
      </view>

      <view class="login-type-switch">
        <text 
          class="switch-text" 
          :class="{ active: loginType === 'password' }"
          @click="loginType = 'password'"
        >密码登录</text>
        <view class="divider"></view>
        <text 
          class="switch-text" 
          :class="{ active: loginType === 'code' }"
          @click="loginType = 'code'"
        >验证码登录</text>
      </view>

      <view class="btn-primary login-btn" @click="handleLogin">
        登 录
      </view>

      <view class="agreement">
        <checkbox :checked="agreed" @click="agreed = !agreed" color="#4FC3F7"></checkbox>
        <text class="agreement-text">我已阅读并同意</text>
        <text class="agreement-link">《用户协议》</text>
        <text class="agreement-text">和</text>
        <text class="agreement-link">《隐私政策》</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loginType: 'password',
      phone: '',
      password: '',
      code: '',
      showPassword: false,
      countdown: 0,
      agreed: false
    }
  },
  onLoad() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = uni.getStorageSync('token')
      if (token) {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }
    },

    md5(str) {
      let hash = 0
      for (let i = 0; i < str.length; i++) {
        const char = str.charCodeAt(i)
        hash = ((hash << 5) - hash) + char
        hash = hash & hash
      }
      return Math.abs(hash).toString(16).padStart(32, '0')
    },

    sendCode() {
      if (!this.phone) {
        uni.showToast({ title: '请输入手机号', icon: 'none' })
        return
      }
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        uni.showToast({ title: '手机号格式不正确', icon: 'none' })
        return
      }
      if (this.countdown > 0) return

      uni.showToast({ title: '验证码已发送', icon: 'success' })
      this.code = '123456'
      this.countdown = 60
      const timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    },

    handleLogin() {
      if (!this.phone) {
        uni.showToast({ title: '请输入手机号', icon: 'none' })
        return
      }
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        uni.showToast({ title: '手机号格式不正确', icon: 'none' })
        return
      }
      if (this.loginType === 'password' && !this.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
      }
      if (this.loginType === 'code' && !this.code) {
        uni.showToast({ title: '请输入验证码', icon: 'none' })
        return
      }
      if (!this.agreed) {
        uni.showToast({ title: '请先同意用户协议', icon: 'none' })
        return
      }

      uni.showLoading({ title: '登录中...' })

      setTimeout(() => {
        uni.hideLoading()

        const userInfo = {
          id: 1,
          phone: this.phone,
          nickname: '代驾用户',
          avatar: '',
          realName: '',
          idCard: '',
          creditLevel: 5,
          balance: 100.50
        }

        const token = this.md5(this.phone + Date.now().toString())

        uni.setStorageSync('token', token)
        uni.setStorageSync('userInfo', userInfo)

        uni.showToast({ title: '登录成功', icon: 'success' })

        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          })
        }, 1000)
      }, 1500)
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #E1F5FE 0%, #FFFFFF 50%);
  padding: 0 40rpx;
  box-sizing: border-box;
}

.login-header {
  padding-top: 120rpx;
  padding-bottom: 80rpx;
  text-align: center;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.logo-icon {
  font-size: 80rpx;
  margin-right: 20rpx;
}

.logo-title {
  font-size: 48rpx;
  font-weight: 600;
  color: $primary-color;
}

.login-subtitle {
  font-size: 28rpx;
  color: $text-muted;
}

.login-form {
  background: $white;
  border-radius: 32rpx;
  padding: 60rpx 40rpx;
  box-shadow: $shadow-lg;
}

.form-item {
  position: relative;
  margin-bottom: 40rpx;
  border-bottom: 2rpx solid $border-color;
  padding-bottom: 20rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: $text-muted;
  margin-bottom: 16rpx;
}

.form-input {
  width: 100%;
  font-size: 32rpx;
  color: $text-color;
  height: 60rpx;
}

.code-input {
  width: 60%;
}

.eye-icon {
  position: absolute;
  right: 0;
  bottom: 20rpx;
  font-size: 40rpx;
}

.code-btn {
  position: absolute;
  right: 0;
  bottom: 16rpx;
  padding: 12rpx 24rpx;
  background: $primary-light;
  color: $primary-color;
  border-radius: 8rpx;
  font-size: 26rpx;

  &.disabled {
    background: $border-color;
    color: $text-muted;
  }
}

.login-type-switch {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 60rpx;
}

.switch-text {
  font-size: 28rpx;
  color: $text-muted;
  padding: 10rpx 20rpx;

  &.active {
    color: $primary-color;
    font-weight: 500;
  }
}

.divider {
  width: 2rpx;
  height: 30rpx;
  background: $border-color;
  margin: 0 20rpx;
}

.login-btn {
  margin-bottom: 40rpx;
}

.agreement {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
}

.agreement-text {
  font-size: 24rpx;
  color: $text-muted;
}

.agreement-link {
  font-size: 24rpx;
  color: $primary-color;
}
</style>
