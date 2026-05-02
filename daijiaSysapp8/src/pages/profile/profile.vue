<template>
  <view class="profile-container">
    <view class="profile-header">
      <view class="user-info" @click="goToPersonalInfo">
        <view class="avatar">
          <text class="avatar-icon" v-if="!userInfo.avatar">👤</text>
          <image v-else :src="userInfo.avatar" class="avatar-img" mode="aspectFill"></image>
        </view>
        <view class="info-content">
          <view class="name-row">
            <text class="user-name">{{ userInfo.nickname || '代驾用户' }}</text>
            <view class="verify-tag" v-if="userInfo.realName">
              <text class="verify-icon">✓</text>
              <text class="verify-text">已实名</text>
            </view>
          </view>
          <text class="user-phone">{{ userInfo.phone ? userInfo.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '未登录' }}</text>
          <view class="credit-level">
            <text class="credit-label">信用等级：</text>
            <view class="stars">
              <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= userInfo.creditLevel }">★</text>
            </view>
            <text class="credit-text">{{ userInfo.creditLevel }}星</text>
          </view>
        </view>
        <text class="arrow">›</text>
      </view>

      <view class="balance-bar">
        <view class="balance-item">
          <text class="balance-label">账户余额</text>
          <text class="balance-value">¥{{ userInfo.balance || '0.00' }}</text>
        </view>
        <view class="balance-item">
          <text class="balance-label">优惠券</text>
          <text class="balance-value">{{ couponCount }}张</text>
        </view>
        <view class="balance-item">
          <text class="balance-label">积分</text>
          <text class="balance-value">{{ points }}</text>
        </view>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goToOrders">
          <view class="menu-icon-box" style="background: #E3F2FD;">
            <text class="menu-icon">📋</text>
          </view>
          <text class="menu-name">我的订单</text>
          <view class="menu-badge" v-if="pendingOrders > 0">{{ pendingOrders }}</view>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToComments">
          <view class="menu-icon-box" style="background: #FFF3E0;">
            <text class="menu-icon">⭐</text>
          </view>
          <text class="menu-name">投诉评价</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToComplaint">
          <view class="menu-icon-box" style="background: #FFEBEE;">
            <text class="menu-icon">📝</text>
          </view>
          <text class="menu-name">投诉单</text>
          <view class="menu-badge" v-if="pendingComplaints > 0">{{ pendingComplaints }}</view>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToEmergency">
          <view class="menu-icon-box" style="background: #FCE4EC;">
            <text class="menu-icon">🆘</text>
          </view>
          <text class="menu-name">紧急求助</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToWallet">
          <view class="menu-icon-box" style="background: #E8F5E9;">
            <text class="menu-icon">💳</text>
          </view>
          <text class="menu-name">我的钱包</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToCoupons">
          <view class="menu-icon-box" style="background: #FFF8E1;">
            <text class="menu-icon">🎫</text>
          </view>
          <text class="menu-name">优惠券</text>
          <text class="menu-value">{{ couponCount }}张</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToSettings">
          <view class="menu-icon-box" style="background: #F3E5F5;">
            <text class="menu-icon">⚙️</text>
          </view>
          <text class="menu-name">设置</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToHelp">
          <view class="menu-icon-box" style="background: #E0F7FA;">
            <text class="menu-icon">❓</text>
          </view>
          <text class="menu-name">帮助中心</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToFeedback">
          <view class="menu-icon-box" style="background: #ECEFF1;">
            <text class="menu-icon">💬</text>
          </view>
          <text class="menu-name">意见反馈</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="logout-section">
      <view class="logout-btn" @click="handleLogout">
        退出登录
      </view>
    </view>

    <view class="version-info">
      <text class="version-text">代驾系统 v1.0.0</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        id: 1,
        phone: '13888888888',
        nickname: '代驾用户',
        avatar: '',
        realName: '张三',
        idCard: '',
        creditLevel: 5,
        balance: 100.50
      },
      couponCount: 3,
      points: 1280,
      pendingOrders: 2,
      pendingComplaints: 1
    }
  },
  onShow() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const savedUser = uni.getStorageSync('userInfo')
      if (savedUser) {
        this.userInfo = savedUser
      }
    },

    goToPersonalInfo() {
      uni.navigateTo({
        url: '/pages/personal-info/personal-info'
      })
    },

    goToOrders() {
      uni.showToast({ title: '订单功能开发中', icon: 'none' })
    },

    goToComments() {
      uni.navigateTo({
        url: '/pages/comments/comments'
      })
    },

    goToComplaint() {
      uni.navigateTo({
        url: '/pages/complaint/complaint'
      })
    },

    goToEmergency() {
      uni.navigateTo({
        url: '/pages/emergency/emergency'
      })
    },

    goToWallet() {
      uni.showToast({ title: '钱包功能开发中', icon: 'none' })
    },

    goToCoupons() {
      uni.showToast({ title: '优惠券功能开发中', icon: 'none' })
    },

    goToSettings() {
      uni.showToast({ title: '设置功能开发中', icon: 'none' })
    },

    goToHelp() {
      uni.showToast({ title: '帮助中心开发中', icon: 'none' })
    },

    goToFeedback() {
      uni.showToast({ title: '意见反馈开发中', icon: 'none' })
    },

    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.redirectTo({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 40rpx;
}

.profile-header {
  background: linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%);
  padding: 40rpx 30rpx;
  padding-bottom: 60rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.avatar {
  width: 140rpx;
  height: 140rpx;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
  overflow: hidden;
}

.avatar-icon {
  font-size: 70rpx;
}

.avatar-img {
  width: 100%;
  height: 100%;
}

.info-content {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.user-name {
  font-size: 36rpx;
  font-weight: 600;
  color: $white;
  margin-right: 20rpx;
}

.verify-tag {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.2);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.verify-icon {
  font-size: 20rpx;
  color: $white;
  margin-right: 6rpx;
}

.verify-text {
  font-size: 22rpx;
  color: $white;
}

.user-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
  display: block;
}

.credit-level {
  display: flex;
  align-items: center;
}

.credit-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-right: 10rpx;
}

.stars {
  margin-right: 10rpx;
}

.star {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.4);
  margin-right: 4rpx;

  &.active {
    color: #FFD700;
  }
}

.credit-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.arrow {
  font-size: 40rpx;
  color: rgba(255, 255, 255, 0.6);
}

.balance-bar {
  display: flex;
  background: $white;
  border-radius: 20rpx;
  padding: 30rpx 0;
  margin: 0 10rpx;
  margin-top: -20rpx;
  box-shadow: $shadow;
}

.balance-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1rpx solid $border-color;

  &:last-child {
    border-right: none;
  }
}

.balance-label {
  font-size: 24rpx;
  color: $text-muted;
  margin-bottom: 10rpx;
}

.balance-value {
  font-size: 36rpx;
  font-weight: 600;
  color: $primary-color;
}

.menu-section {
  padding: 20rpx;
}

.menu-group {
  background: $white;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.menu-icon-box {
  width: 72rpx;
  height: 72rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.menu-icon {
  font-size: 36rpx;
}

.menu-name {
  flex: 1;
  font-size: 30rpx;
  color: $text-color;
}

.menu-value {
  font-size: 26rpx;
  color: $text-muted;
  margin-right: 10rpx;
}

.menu-badge {
  background: $danger-color;
  color: $white;
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  margin-right: 10rpx;
  min-width: 36rpx;
  text-align: center;
}

.menu-arrow {
  font-size: 32rpx;
  color: $text-muted;
}

.logout-section {
  padding: 40rpx 20rpx;
}

.logout-btn {
  background: $white;
  color: $danger-color;
  text-align: center;
  font-size: 32rpx;
  padding: 30rpx;
  border-radius: 20rpx;
}

.version-info {
  text-align: center;
  padding: 20rpx;
}

.version-text {
  font-size: 24rpx;
  color: $text-muted;
}
</style>
