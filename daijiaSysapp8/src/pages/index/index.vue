<template>
  <view class="index-container">
    <view class="header">
      <view class="location-info">
        <text class="location-icon">📍</text>
        <text class="location-text">{{ currentLocation }}</text>
        <text class="arrow">›</text>
      </view>
      <view class="header-actions">
        <view class="action-item" @click="goToMessages">
          <text class="action-icon">🔔</text>
          <view class="badge" v-if="unreadCount > 0">{{ unreadCount }}</view>
        </view>
      </view>
    </view>

    <view class="banner-section">
      <swiper class="banner-swiper" indicator-dots indicator-color="rgba(255,255,255,0.5)" indicator-active-color="#4FC3F7" autoplay circular>
        <swiper-item v-for="(banner, index) in banners" :key="index">
          <view class="banner-item" :style="{ background: banner.bg }">
            <view class="banner-content">
              <text class="banner-title">{{ banner.title }}</text>
              <text class="banner-subtitle">{{ banner.subtitle }}</text>
              <view class="banner-btn">立即查看</view>
            </view>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <view class="quick-actions">
      <view class="action-card" v-for="(action, index) in quickActions" :key="index" @click="handleQuickAction(action)">
        <view class="action-icon-box" :style="{ background: action.bg }">
          <text class="action-emoji">{{ action.icon }}</text>
        </view>
        <text class="action-name">{{ action.name }}</text>
      </view>
    </view>

    <view class="promotion-section">
      <view class="section-header">
        <text class="section-title">优惠活动</text>
        <text class="section-more" @click="goToPromotions">更多 ›</text>
      </view>
      <scroll-view class="promotion-scroll" scroll-x>
        <view class="promotion-list">
          <view class="promotion-card" v-for="(promotion, index) in promotions" :key="index" @click="viewPromotion(promotion)">
            <view class="promotion-tag" :style="{ background: promotion.tagBg }">{{ promotion.tag }}</view>
            <view class="promotion-content">
              <text class="promotion-title">{{ promotion.title }}</text>
              <text class="promotion-desc">{{ promotion.desc }}</text>
              <view class="promotion-footer">
                <text class="promotion-time">有效期至 {{ promotion.endDate }}</text>
                <view class="promotion-btn">立即领取</view>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="coupon-section">
      <view class="section-header">
        <text class="section-title">我的优惠券</text>
        <text class="section-more" @click="goToCoupons">全部 ›</text>
      </view>
      <view class="coupon-list">
        <view class="coupon-card" v-for="(coupon, index) in coupons" :key="index">
          <view class="coupon-left">
            <text class="coupon-amount">¥{{ coupon.amount }}</text>
            <text class="coupon-condition">{{ coupon.condition }}</text>
          </view>
          <view class="coupon-right">
            <text class="coupon-name">{{ coupon.name }}</text>
            <text class="coupon-time">{{ coupon.validTime }}</text>
            <view class="coupon-use-btn" @click="useCoupon(coupon)">立即使用</view>
          </view>
        </view>
      </view>
    </view>

    <view class="recommend-section">
      <view class="section-header">
        <text class="section-title">为你推荐</text>
      </view>
      <view class="recommend-list">
        <view class="recommend-card" v-for="(item, index) in recommends" :key="index">
          <view class="recommend-icon">{{ item.icon }}</view>
          <view class="recommend-info">
            <text class="recommend-title">{{ item.title }}</text>
            <text class="recommend-desc">{{ item.desc }}</text>
          </view>
          <view class="recommend-arrow">›</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentLocation: '北京市朝阳区建国路88号',
      unreadCount: 3,
      banners: [
        {
          title: '新用户专享',
          subtitle: '首单立减30元',
          bg: 'linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%)'
        },
        {
          title: '限时特惠',
          subtitle: '夜间代驾低至5折',
          bg: 'linear-gradient(135deg, #FF9800 0%, #F57C00 100%)'
        },
        {
          title: '邀请好友',
          subtitle: '双方各得20元优惠券',
          bg: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)'
        }
      ],
      quickActions: [
        { name: '立即代驾', icon: '🚗', bg: 'linear-gradient(135deg, #4FC3F7, #29B6F6)', type: 'order' },
        { name: '预约代驾', icon: '📅', bg: 'linear-gradient(135deg, #FF9800, #F57C00)', type: 'reserve' },
        { name: '我的订单', icon: '📋', bg: 'linear-gradient(135deg, #4CAF50, #388E3C)', type: 'orders' },
        { name: '联系客服', icon: '💬', bg: 'linear-gradient(135deg, #9C27B0, #7B1FA2)', type: 'service' }
      ],
      promotions: [
        {
          tag: '限时',
          tagBg: '#FF5722',
          title: '夜间代驾特惠',
          desc: '23:00-次日06:00代驾享5折优惠',
          endDate: '2026-05-31'
        },
        {
          tag: '满减',
          tagBg: '#E91E63',
          title: '满100减20',
          desc: '单次代驾满100元立减20元',
          endDate: '2026-06-15'
        },
        {
          tag: '新人',
          tagBg: '#4CAF50',
          title: '新用户专享',
          desc: '注册即送50元代驾大礼包',
          endDate: '长期有效'
        }
      ],
      coupons: [
        {
          amount: 20,
          condition: '满80可用',
          name: '通用代驾券',
          validTime: '有效期至 2026-05-30'
        },
        {
          amount: 30,
          condition: '满120可用',
          name: '商务代驾券',
          validTime: '有效期至 2026-06-15'
        }
      ],
      recommends: [
        { icon: '⭐', title: '安全保障', desc: '全程行程监控，安全有保障' },
        { icon: '💳', title: '支付方式', desc: '支持微信、支付宝等多种支付' },
        { icon: '🏆', title: '司机服务', desc: '专业培训，优质服务体验' }
      ]
    }
  },
  onLoad() {
    this.checkLogin()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.redirectTo({
          url: '/pages/login/login'
        })
      }
    },

    handleQuickAction(action) {
      switch(action.type) {
        case 'order':
        case 'reserve':
          uni.switchTab({
            url: '/pages/order/order'
          })
          break
        case 'orders':
          uni.showToast({ title: '订单功能开发中', icon: 'none' })
          break
        case 'service':
          uni.showToast({ title: '客服功能开发中', icon: 'none' })
          break
      }
    },

    goToMessages() {
      uni.showToast({ title: '消息中心开发中', icon: 'none' })
    },

    goToPromotions() {
      uni.showToast({ title: '更多优惠开发中', icon: 'none' })
    },

    viewPromotion(promotion) {
      uni.showToast({ title: promotion.title, icon: 'none' })
    },

    goToCoupons() {
      uni.showToast({ title: '全部优惠券', icon: 'none' })
    },

    useCoupon(coupon) {
      uni.switchTab({
        url: '/pages/order/order'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.index-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 40rpx;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background: $white;
}

.location-info {
  display: flex;
  align-items: center;
}

.location-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.location-text {
  font-size: 28rpx;
  color: $text-color;
  max-width: 400rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.arrow {
  font-size: 32rpx;
  color: $text-muted;
  margin-left: 10rpx;
}

.header-actions {
  display: flex;
  align-items: center;
}

.action-item {
  position: relative;
  padding: 10rpx;
}

.action-icon {
  font-size: 40rpx;
}

.badge {
  position: absolute;
  top: 0;
  right: 0;
  background: $danger-color;
  color: $white;
  font-size: 20rpx;
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
  min-width: 30rpx;
  text-align: center;
}

.banner-section {
  padding: 20rpx 30rpx;
}

.banner-swiper {
  height: 280rpx;
  border-radius: 24rpx;
  overflow: hidden;
}

.banner-item {
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 40rpx;
}

.banner-content {
  flex: 1;
}

.banner-title {
  display: block;
  font-size: 40rpx;
  font-weight: 600;
  color: $white;
  margin-bottom: 10rpx;
}

.banner-subtitle {
  display: block;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 20rpx;
}

.banner-btn {
  display: inline-block;
  background: $white;
  color: $primary-color;
  font-size: 26rpx;
  padding: 12rpx 30rpx;
  border-radius: 30rpx;
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  padding: 20rpx 30rpx;
  background: $white;
  margin-bottom: 20rpx;
}

.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.action-icon-box {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.action-emoji {
  font-size: 48rpx;
}

.action-name {
  font-size: 26rpx;
  color: $text-color;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  padding-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: $text-color;
}

.section-more {
  font-size: 26rpx;
  color: $text-muted;
}

.promotion-section {
  background: $white;
  margin-bottom: 20rpx;
}

.promotion-scroll {
  white-space: nowrap;
}

.promotion-list {
  display: inline-flex;
  padding: 0 30rpx 30rpx;
  gap: 20rpx;
}

.promotion-card {
  width: 420rpx;
  background: linear-gradient(135deg, #F5F9FF 0%, #E8F4FD 100%);
  border-radius: 20rpx;
  padding: 24rpx;
  position: relative;
  display: inline-block;
  white-space: normal;
}

.promotion-tag {
  position: absolute;
  top: 0;
  right: 0;
  color: $white;
  font-size: 22rpx;
  padding: 8rpx 20rpx;
  border-radius: 0 20rpx 0 20rpx;
}

.promotion-content {
  padding-top: 20rpx;
}

.promotion-title {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: $text-color;
  margin-bottom: 10rpx;
}

.promotion-desc {
  display: block;
  font-size: 24rpx;
  color: $text-muted;
  margin-bottom: 20rpx;
}

.promotion-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.promotion-time {
  font-size: 22rpx;
  color: $text-muted;
}

.promotion-btn {
  background: $primary-color;
  color: $white;
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.coupon-section {
  background: $white;
  padding-bottom: 20rpx;
  margin-bottom: 20rpx;
}

.coupon-list {
  padding: 0 30rpx;
}

.coupon-card {
  display: flex;
  background: linear-gradient(90deg, #FFF8F0 0%, #FFF 100%);
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    left: 180rpx;
    top: -10rpx;
    width: 20rpx;
    height: 20rpx;
    background: $background-color;
    border-radius: 50%;
  }

  &::after {
    content: '';
    position: absolute;
    left: 180rpx;
    bottom: -10rpx;
    width: 20rpx;
    height: 20rpx;
    background: $background-color;
    border-radius: 50%;
  }
}

.coupon-left {
  width: 200rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30rpx 0;
  border-right: 2rpx dashed $border-color;
}

.coupon-amount {
  font-size: 48rpx;
  font-weight: 600;
  color: $primary-color;
}

.coupon-condition {
  font-size: 22rpx;
  color: $text-muted;
  margin-top: 8rpx;
}

.coupon-right {
  flex: 1;
  padding: 30rpx;
  display: flex;
  flex-direction: column;
}

.coupon-name {
  font-size: 30rpx;
  font-weight: 500;
  color: $text-color;
  margin-bottom: 10rpx;
}

.coupon-time {
  font-size: 22rpx;
  color: $text-muted;
  margin-bottom: 16rpx;
}

.coupon-use-btn {
  align-self: flex-start;
  background: $primary-color;
  color: $white;
  font-size: 24rpx;
  padding: 10rpx 28rpx;
  border-radius: 30rpx;
}

.recommend-section {
  background: $white;
}

.recommend-list {
  padding: 0 30rpx 30rpx;
}

.recommend-card {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.recommend-icon {
  font-size: 44rpx;
  margin-right: 20rpx;
}

.recommend-info {
  flex: 1;
}

.recommend-title {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 6rpx;
}

.recommend-desc {
  display: block;
  font-size: 24rpx;
  color: $text-muted;
}

.recommend-arrow {
  font-size: 36rpx;
  color: $text-muted;
}
</style>
