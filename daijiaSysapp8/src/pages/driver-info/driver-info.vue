<template>
  <view class="driver-info-container">
    <view class="driver-header">
      <view class="driver-avatar-section">
        <view class="avatar-wrapper">
          <view class="driver-avatar">
            <text class="avatar-icon">👨‍✈️</text>
          </view>
          <view class="online-badge" v-if="driverInfo.isOnline">
            <text class="online-dot"></text>
            <text class="online-text">在线</text>
          </view>
        </view>
        <view class="driver-basic-info">
          <view class="name-row">
            <text class="driver-name">{{ driverInfo.name }}</text>
            <view class="gender-tag" v-if="driverInfo.gender === '女'">
              <text class="gender-icon">👩</text>
            </view>
          </view>
          <view class="credit-row">
            <view class="stars">
              <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= driverInfo.creditLevel }">★</text>
            </view>
            <text class="credit-text">{{ driverInfo.creditLevel }}星司机</text>
          </view>
          <text class="driver-phone">{{ driverInfo.phone }}</text>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="stat-item">
        <text class="stat-value">{{ driverInfo.orderCount }}</text>
        <text class="stat-label">接单次数</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-value">{{ driverInfo.rating }}%</text>
        <text class="stat-label">好评率</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-value">{{ driverInfo.experience }}年</text>
        <text class="stat-label">驾龄</text>
      </view>
    </view>

    <view class="car-section">
      <view class="section-header">
        <text class="section-icon">🚗</text>
        <text class="section-title">车辆信息</text>
      </view>
      <view class="car-info-card">
        <view class="car-preview">
          <view class="car-image">
            <text class="car-icon">🚘</text>
          </view>
          <view class="car-type-tag">{{ driverInfo.carLevel }}</view>
        </view>
        <view class="car-details">
          <view class="detail-row">
            <text class="detail-label">车型</text>
            <text class="detail-value">{{ driverInfo.carModel }}</text>
          </view>
          <view class="detail-row">
            <text class="detail-label">车牌号</text>
            <text class="detail-value">{{ driverInfo.carPlate }}</text>
          </view>
          <view class="detail-row">
            <text class="detail-label">车身颜色</text>
            <text class="detail-value">{{ driverInfo.carColor }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="tags-section">
      <view class="section-header">
        <text class="section-icon">🏷️</text>
        <text class="section-title">服务标签</text>
      </view>
      <view class="tags-list">
        <view class="tag-item" v-for="(tag, index) in driverInfo.serviceTags" :key="index">
          <text class="tag-icon">{{ tag.icon }}</text>
          <text class="tag-text">{{ tag.text }}</text>
        </view>
      </view>
    </view>

    <view class="cert-section">
      <view class="section-header">
        <text class="section-icon">✅</text>
        <text class="section-title">资质认证</text>
      </view>
      <view class="cert-list">
        <view class="cert-item" v-for="(cert, index) in driverInfo.certificates" :key="index">
          <view class="cert-icon-box">
            <text class="cert-icon">{{ cert.icon }}</text>
          </view>
          <view class="cert-info">
            <text class="cert-name">{{ cert.name }}</text>
            <text class="cert-status" :class="cert.status">{{ cert.statusText }}</text>
          </view>
          <view class="cert-check" v-if="cert.status === 'valid'">✓</view>
        </view>
      </view>
    </view>

    <view class="comments-section">
      <view class="section-header">
        <text class="section-icon">💬</text>
        <text class="section-title">用户评价</text>
        <text class="section-more" @click="viewAllComments">查看全部 ›</text>
      </view>
      <view class="comments-list">
        <view class="comment-card" v-for="(comment, index) in comments" :key="index">
          <view class="comment-header">
            <view class="user-avatar">
              <text class="user-icon">👤</text>
            </view>
            <view class="user-info">
              <text class="user-name">{{ comment.userName }}</text>
              <view class="comment-stars">
                <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= comment.rating }">★</text>
              </view>
            </view>
            <text class="comment-time">{{ comment.time }}</text>
          </view>
          <view class="comment-content">
            <text class="content-text">{{ comment.content }}</text>
          </view>
          <view class="comment-tags" v-if="comment.tags && comment.tags.length > 0">
            <view class="comment-tag" v-for="(tag, tagIndex) in comment.tags" :key="tagIndex">
              {{ tag }}
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-actions">
      <view class="action-btn call" @click="callDriver">
        <text class="action-icon">📞</text>
        <text class="action-text">联系司机</text>
      </view>
      <view class="action-btn chat" @click="chatWithDriver">
        <text class="action-icon">💬</text>
        <text class="action-text">发送消息</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      driverInfo: {
        id: 1,
        name: '张师傅',
        gender: '男',
        phone: '138****8888',
        avatar: '',
        creditLevel: 5,
        orderCount: 1286,
        rating: 98.5,
        experience: 8,
        isOnline: true,
        carModel: '奔驰 E300L',
        carPlate: '京A·88888',
        carColor: '黑色',
        carLevel: '商务型',
        serviceTags: [
          { icon: '🎯', text: '准时达' },
          { icon: '💼', text: '商务服务' },
          { icon: '🛡️', text: '安全驾驶' },
          { icon: '😊', text: '服务热情' }
        ],
        certificates: [
          { icon: '🪪', name: '身份证', status: 'valid', statusText: '已认证' },
          { icon: '🚗', name: '驾驶证', status: 'valid', statusText: '已认证' },
          { icon: '📋', name: '从业资格证', status: 'valid', statusText: '已认证' }
        ]
      },
      comments: [
        {
          id: 1,
          userName: '王先生',
          rating: 5,
          time: '2026-05-01',
          content: '张师傅驾驶技术非常好，服务态度也很棒，全程平稳，提前到达，下次还会选择这位师傅。',
          tags: ['驾驶技术好', '服务态度好', '准时']
        },
        {
          id: 2,
          userName: '李女士',
          rating: 5,
          time: '2026-04-28',
          content: '师傅很专业，帮忙搬行李，全程服务周到，车子也很干净舒适，非常满意的一次代驾体验。',
          tags: ['服务周到', '车内整洁', '专业']
        },
        {
          id: 3,
          userName: '赵先生',
          rating: 4,
          time: '2026-04-25',
          content: '整体服务不错，司机准时到达，驾驶平稳。唯一建议是可以再熟悉一下路线。',
          tags: ['准时', '驾驶平稳']
        }
      ]
    }
  },
  onLoad(options) {
    if (options.id) {
      this.loadDriverInfo(options.id)
    }
  },
  methods: {
    loadDriverInfo(id) {
      console.log('加载司机信息:', id)
    },

    callDriver() {
      uni.makePhoneCall({
        phoneNumber: '13888888888',
        fail: () => {
          uni.showToast({ title: '拨打电话失败', icon: 'none' })
        }
      })
    },

    chatWithDriver() {
      uni.showToast({ title: '聊天功能开发中', icon: 'none' })
    },

    viewAllComments() {
      uni.showToast({ title: '全部评价开发中', icon: 'none' })
    }
  }
}
</script>

<style lang="scss" scoped>
.driver-info-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 140rpx;
}

.driver-header {
  background: linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%);
  padding: 40rpx 30rpx;
  padding-bottom: 80rpx;
}

.driver-avatar-section {
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  position: relative;
}

.driver-avatar {
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 80rpx;
}

.online-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  display: flex;
  align-items: center;
  background: $white;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.online-dot {
  width: 12rpx;
  height: 12rpx;
  background: $success-color;
  border-radius: 50%;
  margin-right: 8rpx;
}

.online-text {
  font-size: 22rpx;
  color: $success-color;
}

.driver-basic-info {
  margin-left: 30rpx;
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.driver-name {
  font-size: 36rpx;
  font-weight: 600;
  color: $white;
  margin-right: 16rpx;
}

.gender-tag {
  padding: 4rpx 12rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8rpx;
}

.gender-icon {
  font-size: 24rpx;
}

.credit-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.stars {
  display: flex;
  margin-right: 12rpx;
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
  color: rgba(255, 255, 255, 0.9);
}

.driver-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.stats-section {
  display: flex;
  background: $white;
  margin: -40rpx 30rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx 0;
  box-shadow: $shadow;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 40rpx;
  font-weight: 600;
  color: $primary-color;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: $text-muted;
}

.stat-divider {
  width: 1rpx;
  background: $border-color;
  margin: 10rpx 0;
}

.car-section,
.tags-section,
.cert-section,
.comments-section {
  background: $white;
  margin-bottom: 20rpx;
  padding: 0 30rpx;
}

.section-header {
  display: flex;
  align-items: center;
  padding: 30rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.section-icon {
  font-size: 36rpx;
  margin-right: 12rpx;
}

.section-title {
  flex: 1;
  font-size: 30rpx;
  font-weight: 600;
  color: $text-color;
}

.section-more {
  font-size: 24rpx;
  color: $text-muted;
}

.car-info-card {
  padding: 30rpx 0;
}

.car-preview {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30rpx;
  padding: 24rpx;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  border-radius: 16rpx;
}

.car-image {
  width: 160rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.car-icon {
  font-size: 80rpx;
}

.car-type-tag {
  font-size: 26rpx;
  color: $primary-color;
  background: $white;
  padding: 8rpx 20rpx;
  border-radius: 30rpx;
}

.car-details {
  padding: 0 10rpx;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.detail-label {
  font-size: 28rpx;
  color: $text-muted;
}

.detail-value {
  font-size: 28rpx;
  color: $text-color;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  padding: 30rpx 0;
}

.tag-item {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  border-radius: 50rpx;
}

.tag-icon {
  font-size: 28rpx;
  margin-right: 10rpx;
}

.tag-text {
  font-size: 26rpx;
  color: $primary-color;
}

.cert-list {
  padding: 20rpx 0;
}

.cert-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.cert-icon-box {
  width: 64rpx;
  height: 64rpx;
  background: #E3F2FD;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.cert-icon {
  font-size: 32rpx;
}

.cert-info {
  flex: 1;
}

.cert-name {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 4rpx;
}

.cert-status {
  font-size: 24rpx;

  &.valid {
    color: $success-color;
  }

  &.invalid {
    color: $danger-color;
  }
}

.cert-check {
  width: 40rpx;
  height: 40rpx;
  background: $success-color;
  border-radius: 50%;
  color: $white;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.comments-list {
  padding: 20rpx 0;
}

.comment-card {
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.user-avatar {
  width: 64rpx;
  height: 64rpx;
  background: $background-color;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}

.user-icon {
  font-size: 32rpx;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 6rpx;
}

.comment-stars {
  display: flex;
}

.comment-stars .star {
  font-size: 24rpx;
  color: $border-color;
  margin-right: 2rpx;

  &.active {
    color: #FFD700;
  }
}

.comment-time {
  font-size: 24rpx;
  color: $text-muted;
}

.comment-content {
  margin-bottom: 16rpx;
}

.content-text {
  font-size: 28rpx;
  color: $text-color;
  line-height: 1.6;
}

.comment-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.comment-tag {
  font-size: 24rpx;
  color: $primary-color;
  background: rgba(79, 195, 247, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: $white;
  gap: 20rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx 0;
  border-radius: 50rpx;

  &.call {
    background: $primary-color;
    color: $white;
  }

  &.chat {
    background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
    color: $white;
  }
}

.action-icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.action-text {
  font-size: 30rpx;
  font-weight: 500;
}
</style>
