<template>
  <view class="personal-info-container">
    <view class="avatar-section">
      <view class="avatar-wrapper" @click="changeAvatar">
        <view class="avatar">
          <text class="avatar-icon" v-if="!userInfo.avatar">👤</text>
          <image v-else :src="userInfo.avatar" class="avatar-img" mode="aspectFill"></image>
        </view>
        <view class="avatar-edit">
          <text class="edit-icon">📷</text>
        </view>
      </view>
      <text class="avatar-hint">点击更换头像</text>
    </view>

    <view class="info-section">
      <view class="info-item">
        <text class="info-label">昵称</text>
        <view class="info-value-wrapper" @click="editNickname">
          <text class="info-value">{{ userInfo.nickname || '未设置' }}</text>
          <text class="info-arrow">›</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">手机号</text>
        <view class="info-value-wrapper">
          <text class="info-value">{{ userInfo.phone ? userInfo.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '未绑定' }}</text>
          <view class="info-tag" v-if="userInfo.phone">已绑定</view>
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">性别</text>
        <view class="info-value-wrapper" @click="selectGender">
          <text class="info-value">{{ genderText }}</text>
          <text class="info-arrow">›</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">生日</text>
        <picker mode="date" :value="birthday" @change="onBirthdayChange">
          <view class="info-value-wrapper">
            <text class="info-value">{{ userInfo.birthday || '未设置' }}</text>
            <text class="info-arrow">›</text>
          </view>
        </picker>
      </view>
    </view>

    <view class="verify-section">
      <view class="section-header">
        <text class="section-title">实名认证</text>
      </view>
      <view class="verify-item">
        <view class="verify-icon-box">
          <text class="verify-icon">🪪</text>
        </view>
        <view class="verify-content">
          <text class="verify-title">身份认证</text>
          <text class="verify-desc">{{ userInfo.realName ? '已认证' : '未认证' }}</text>
        </view>
        <view class="verify-status" :class="{ verified: userInfo.realName }" @click="goToVerify">
          <text v-if="userInfo.realName">已认证</text>
          <text v-else>去认证</text>
        </view>
      </view>
    </view>

    <view class="credit-section">
      <view class="section-header">
        <text class="section-title">信用等级</text>
        <text class="section-more" @click="goToCreditDetail">查看详情 ›</text>
      </view>
      <view class="credit-card">
        <view class="credit-header">
          <text class="credit-level-text">信用等级</text>
          <view class="credit-badge">{{ userInfo.creditLevel }}星用户</view>
        </view>
        <view class="credit-stars">
          <view class="star-item" v-for="i in 5" :key="i">
            <text class="star-icon" :class="{ active: i <= userInfo.creditLevel }">★</text>
            <text class="star-label">{{ i }}星</text>
          </view>
        </view>
        <view class="credit-progress">
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: progressWidth + '%' }"></view>
          </view>
          <text class="progress-text">当前积分: {{ creditPoints }}分</text>
        </view>
      </view>
    </view>

    <view class="safe-section">
      <view class="section-header">
        <text class="section-title">账号安全</text>
      </view>
      <view class="safe-item" @click="changePassword">
        <text class="safe-icon">🔐</text>
        <text class="safe-name">修改密码</text>
        <text class="safe-arrow">›</text>
      </view>
      <view class="safe-item" @click="goToSafetyCenter">
        <text class="safe-icon">🛡️</text>
        <text class="safe-name">安全中心</text>
        <text class="safe-arrow">›</text>
      </view>
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
        idCard: '110101********1234',
        gender: 1,
        birthday: '1990-01-01',
        creditLevel: 5
      },
      birthday: '1990-01-01',
      creditPoints: 1280
    }
  },
  computed: {
    genderText() {
      const genderMap = { 0: '未知', 1: '男', 2: '女' }
      return genderMap[this.userInfo.gender] || '未设置'
    },
    progressWidth() {
      const maxPoints = 2000
      return Math.min((this.creditPoints / maxPoints) * 100, 100)
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const savedUser = uni.getStorageSync('userInfo')
      if (savedUser) {
        this.userInfo = { ...this.userInfo, ...savedUser }
      }
    },

    changeAvatar() {
      uni.showActionSheet({
        itemList: ['拍照', '从相册选择'],
        success: (res) => {
          const sourceType = res.tapIndex === 0 ? ['camera'] : ['album']
          uni.chooseImage({
            count: 1,
            sizeType: ['compressed'],
            sourceType: sourceType,
            success: (chooseRes) => {
              const tempFilePath = chooseRes.tempFilePaths[0]
              this.userInfo.avatar = tempFilePath
              uni.showToast({ title: '头像更新成功', icon: 'success' })
            }
          })
        }
      })
    },

    editNickname() {
      uni.showModal({
        title: '修改昵称',
        editable: true,
        placeholderText: this.userInfo.nickname,
        success: (res) => {
          if (res.confirm && res.content) {
            this.userInfo.nickname = res.content
            uni.showToast({ title: '修改成功', icon: 'success' })
          }
        }
      })
    },

    selectGender() {
      uni.showActionSheet({
        itemList: ['男', '女', '保密'],
        success: (res) => {
          const genderMap = { 0: 1, 1: 2, 2: 0 }
          this.userInfo.gender = genderMap[res.tapIndex]
          uni.showToast({ title: '修改成功', icon: 'success' })
        }
      })
    },

    onBirthdayChange(e) {
      this.userInfo.birthday = e.detail.value
      this.birthday = e.detail.value
      uni.showToast({ title: '修改成功', icon: 'success' })
    },

    goToVerify() {
      if (this.userInfo.realName) {
        uni.showModal({
          title: '实名认证信息',
          content: `姓名: ${this.userInfo.realName}\n身份证: ${this.userInfo.idCard}`,
          showCancel: false
        })
      } else {
        uni.showToast({ title: '实名认证功能开发中', icon: 'none' })
      }
    },

    goToCreditDetail() {
      uni.showToast({ title: '信用详情开发中', icon: 'none' })
    },

    changePassword() {
      uni.showToast({ title: '修改密码功能开发中', icon: 'none' })
    },

    goToSafetyCenter() {
      uni.showToast({ title: '安全中心开发中', icon: 'none' })
    }
  }
}
</script>

<style lang="scss" scoped>
.personal-info-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 40rpx;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
  background: $white;
  margin-bottom: 20rpx;
}

.avatar-wrapper {
  position: relative;
}

.avatar {
  width: 200rpx;
  height: 200rpx;
  background: linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.avatar-icon {
  font-size: 100rpx;
}

.avatar-img {
  width: 100%;
  height: 100%;
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 60rpx;
  height: 60rpx;
  background: $primary-color;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid $white;
}

.edit-icon {
  font-size: 32rpx;
}

.avatar-hint {
  font-size: 24rpx;
  color: $text-muted;
  margin-top: 20rpx;
}

.info-section {
  background: $white;
  margin-bottom: 20rpx;
  padding: 0 30rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.info-label {
  font-size: 28rpx;
  color: $text-color;
}

.info-value-wrapper {
  display: flex;
  align-items: center;
}

.info-value {
  font-size: 28rpx;
  color: $text-muted;
}

.info-arrow {
  font-size: 32rpx;
  color: $text-muted;
  margin-left: 10rpx;
}

.info-tag {
  font-size: 22rpx;
  color: $success-color;
  background: rgba(76, 175, 80, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  margin-left: 16rpx;
}

.verify-section,
.credit-section,
.safe-section {
  background: $white;
  margin-bottom: 20rpx;
  padding: 0 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: $text-color;
}

.section-more {
  font-size: 24rpx;
  color: $text-muted;
}

.verify-item {
  display: flex;
  align-items: center;
  padding: 30rpx 0;
}

.verify-icon-box {
  width: 72rpx;
  height: 72rpx;
  background: #E3F2FD;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.verify-icon {
  font-size: 36rpx;
}

.verify-content {
  flex: 1;
}

.verify-title {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 6rpx;
}

.verify-desc {
  font-size: 24rpx;
  color: $text-muted;
}

.verify-status {
  font-size: 26rpx;
  color: $primary-color;
  background: rgba(79, 195, 247, 0.1);
  padding: 10rpx 24rpx;
  border-radius: 30rpx;

  &.verified {
    color: $success-color;
    background: rgba(76, 175, 80, 0.1);
  }
}

.credit-card {
  padding: 30rpx 0;
}

.credit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.credit-level-text {
  font-size: 28rpx;
  color: $text-color;
}

.credit-badge {
  font-size: 26rpx;
  color: $primary-color;
  background: rgba(79, 195, 247, 0.1);
  padding: 10rpx 24rpx;
  border-radius: 30rpx;
}

.credit-stars {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.star-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.star-icon {
  font-size: 40rpx;
  color: $border-color;
  margin-bottom: 8rpx;

  &.active {
    color: #FFD700;
  }
}

.star-label {
  font-size: 22rpx;
  color: $text-muted;
}

.credit-progress {
  text-align: center;
}

.progress-bar {
  height: 12rpx;
  background: $border-color;
  border-radius: 6rpx;
  overflow: hidden;
  margin-bottom: 16rpx;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4FC3F7 0%, #29B6F6 100%);
  border-radius: 6rpx;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 24rpx;
  color: $text-muted;
}

.safe-item {
  display: flex;
  align-items: center;
  padding: 30rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.safe-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.safe-name {
  flex: 1;
  font-size: 28rpx;
  color: $text-color;
}

.safe-arrow {
  font-size: 32rpx;
  color: $text-muted;
}
</style>
