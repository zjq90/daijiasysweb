<template>
  <view class="order-container">
    <view class="map-section">
      <view class="map-placeholder">
        <text class="map-icon">🗺️</text>
        <text class="map-text">地图区域</text>
      </view>
      <view class="location-panel">
        <view class="location-item start">
          <view class="location-dot start-dot"></view>
          <view class="location-content">
            <text class="location-label">出发地</text>
            <input class="location-input" placeholder="请输入出发地" v-model="startLocation" />
          </view>
          <view class="location-actions">
            <text class="action-icon" @click="useCurrentLocation">📍</text>
          </view>
        </view>
        <view class="location-divider"></view>
        <view class="location-item end">
          <view class="location-dot end-dot"></view>
          <view class="location-content">
            <text class="location-label">目的地</text>
            <input class="location-input" placeholder="请输入目的地" v-model="endLocation" />
          </view>
          <view class="location-actions">
            <text class="action-icon" @click="clearDestination">✕</text>
          </view>
        </view>
      </view>
    </view>

    <view class="order-options" v-if="!hasDriverAccepted">
      <view class="option-section">
        <view class="section-title">
          <text class="title-icon">⏰</text>
          <text class="title-text">服务模式</text>
        </view>
        <view class="mode-options">
          <view class="mode-item" :class="{ active: orderMode === 'immediate' }" @click="orderMode = 'immediate'">
            <text class="mode-icon">🚀</text>
            <text class="mode-name">立即代驾</text>
            <view class="mode-check" v-if="orderMode === 'immediate'">✓</view>
          </view>
          <view class="mode-item" :class="{ active: orderMode === 'reserve' }" @click="orderMode = 'reserve'">
            <text class="mode-icon">📅</text>
            <text class="mode-name">预约代驾</text>
            <view class="mode-check" v-if="orderMode === 'reserve'">✓</view>
          </view>
        </view>
        
        <view class="datetime-section" v-if="orderMode === 'reserve'">
          <picker mode="date" :value="reserveDate" @change="onDateChange">
            <view class="datetime-item">
              <text class="datetime-label">预约日期</text>
              <text class="datetime-value">{{ reserveDate }}</text>
              <text class="datetime-arrow">›</text>
            </view>
          </picker>
          <picker mode="time" :value="reserveTime" @change="onTimeChange">
            <view class="datetime-item">
              <text class="datetime-label">预约时间</text>
              <text class="datetime-value">{{ reserveTime }}</text>
              <text class="datetime-arrow">›</text>
            </view>
          </picker>
        </view>
      </view>

      <view class="option-section">
        <view class="section-title">
          <text class="title-icon">🚗</text>
          <text class="title-text">车型选择</text>
        </view>
        <view class="car-options">
          <view class="car-item" :class="{ active: selectedCar === car.type }" v-for="car in carTypes" :key="car.type" @click="selectedCar = car.type">
            <text class="car-icon">{{ car.icon }}</text>
            <text class="car-name">{{ car.name }}</text>
            <text class="car-price">¥{{ car.price }}/公里</text>
          </view>
        </view>
      </view>

      <view class="option-section">
        <view class="section-title">
          <text class="title-icon">👤</text>
          <text class="title-text">服务类型</text>
        </view>
        <view class="service-options">
          <view class="service-item" :class="{ active: selectedService === service.type }" v-for="service in serviceTypes" :key="service.type" @click="selectedService = service.type">
            <view class="service-icon-box" :style="{ background: service.bg }">
              <text class="service-icon">{{ service.icon }}</text>
            </view>
            <text class="service-name">{{ service.name }}</text>
            <text class="service-desc">{{ service.desc }}</text>
          </view>
        </view>
      </view>

      <view class="estimate-section">
        <view class="estimate-header">
          <text class="estimate-title">预估费用</text>
          <view class="estimate-info">
            <text class="estimate-distance">约 {{ estimatedDistance }} 公里</text>
            <text class="estimate-time">预计 {{ estimatedTime }} 分钟</text>
          </view>
        </view>
        <view class="estimate-breakdown">
          <view class="breakdown-item">
            <text class="breakdown-label">起步价</text>
            <text class="breakdown-value">¥{{ basePrice }}</text>
          </view>
          <view class="breakdown-item">
            <text class="breakdown-label">里程费 ({{ estimatedDistance }}公里)</text>
            <text class="breakdown-value">¥{{ distanceFee }}</text>
          </view>
          <view class="breakdown-item" v-if="serviceExtra > 0">
            <text class="breakdown-label">服务费</text>
            <text class="breakdown-value">+¥{{ serviceExtra }}</text>
          </view>
          <view class="breakdown-item total">
            <text class="breakdown-label">预估总价</text>
            <text class="breakdown-total">¥{{ estimatedPrice }}</text>
          </view>
        </view>
        <view class="coupon-bar" @click="selectCoupon">
          <text class="coupon-icon">🎫</text>
          <text class="coupon-text" v-if="selectedCoupon">已选 ¥{{ selectedCoupon.amount }} 优惠券</text>
          <text class="coupon-text coupon-placeholder" v-else>选择优惠券</text>
          <text class="coupon-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="driver-section" v-else>
      <view class="driver-status-bar">
        <view class="status-dot"></view>
        <text class="status-text">司机已接单</text>
        <view class="status-timer">{{ waitingTime }}</view>
      </view>
      
      <view class="driver-card">
        <view class="driver-header">
          <view class="driver-avatar">
            <text class="avatar-icon">👨‍✈️</text>
          </view>
          <view class="driver-info">
            <view class="driver-name-row">
              <text class="driver-name">{{ acceptedDriver.name }}</text>
              <view class="driver-tag">
                <text class="tag-icon">⭐</text>
                <text class="tag-text">{{ acceptedDriver.creditLevel }}星</text>
              </view>
            </view>
            <text class="driver-phone">{{ acceptedDriver.phone }}</text>
            <view class="driver-stats">
              <text class="stat-item">接单 {{ acceptedDriver.orderCount }} 次</text>
              <text class="stat-divider">|</text>
              <text class="stat-item">好评率 {{ acceptedDriver.rating }}%</text>
            </view>
          </view>
          <view class="driver-contact" @click="callDriver">
            <text class="contact-icon">📞</text>
            <text class="contact-text">联系</text>
          </view>
        </view>

        <view class="driver-car-info">
          <view class="car-info-item">
            <text class="car-info-icon">🚘</text>
            <text class="car-info-text">{{ acceptedDriver.carModel }}</text>
          </view>
          <view class="car-info-item">
            <text class="car-info-icon">🅿️</text>
            <text class="car-info-text">{{ acceptedDriver.carPlate }}</text>
          </view>
        </view>

        <view class="driver-location">
          <text class="location-label">司机位置</text>
          <text class="location-value">距离您约 {{ acceptedDriver.distance }} 公里，预计 {{ acceptedDriver.arriveTime }} 分钟到达</text>
        </view>

        <view class="trip-info">
          <view class="trip-item">
            <view class="trip-dot start"></view>
            <text class="trip-text">{{ startLocation }}</text>
          </view>
          <view class="trip-line"></view>
          <view class="trip-item">
            <view class="trip-dot end"></view>
            <text class="trip-text">{{ endLocation }}</text>
          </view>
        </view>
      </view>

      <view class="emergency-bar" @click="goToEmergency">
        <text class="emergency-icon">🆘</text>
        <text class="emergency-text">紧急求助</text>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="price-info" v-if="!hasDriverAccepted">
        <text class="price-label">预估费用</text>
        <text class="price-value">¥{{ estimatedPrice }}</text>
        <text class="price-unit">起</text>
      </view>
      <view class="submit-btn" :class="{ disabled: !canSubmit }" @click="submitOrder">
        {{ buttonText }}
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderMode: 'immediate',
      reserveDate: '2026-05-03',
      reserveTime: '18:00',
      startLocation: '北京市朝阳区建国路88号SOHO现代城',
      endLocation: '',
      selectedCar: 'standard',
      selectedService: 'normal',
      selectedCoupon: null,
      hasDriverAccepted: false,
      waitingTime: '00:00',
      waitingTimer: null,
      acceptedDriver: {
        name: '张师傅',
        phone: '138****8888',
        creditLevel: 5,
        orderCount: 1286,
        rating: 98,
        carModel: '奔驰 E300L',
        carPlate: '京A·88888',
        distance: 1.2,
        arriveTime: 5
      },
      carTypes: [
        { type: 'standard', name: '经济型', icon: '🚗', price: 3.5 },
        { type: 'comfort', name: '舒适型', icon: '🚙', price: 4.5 },
        { type: 'business', name: '商务型', icon: '🚘', price: 6.0 }
      ],
      serviceTypes: [
        { type: 'normal', name: '普通代驾', icon: '👤', desc: '标准代驾服务', bg: '#E3F2FD' },
        { type: 'business', name: '商务代驾', icon: '💼', desc: '专业商务服务', bg: '#FFF3E0' },
        { type: 'female', name: '女性专属', icon: '👩', desc: '女司机代驾', bg: '#FCE4EC' },
        { type: 'vip', name: 'VIP服务', icon: '👑', desc: '尊享VIP服务', bg: '#FFF8E1' }
      ],
      estimatedDistance: 12.5,
      estimatedTime: 35,
      basePrice: 28,
      distanceFee: 0,
      serviceExtra: 0
    }
  },
  computed: {
    distanceFee() {
      return (this.estimatedDistance * this.currentCarPrice).toFixed(2)
    },
    currentCarPrice() {
      const car = this.carTypes.find(c => c.type === this.selectedCar)
      return car ? car.price : 3.5
    },
    estimatedPrice() {
      let total = parseFloat(this.basePrice) + parseFloat(this.distanceFee) + this.serviceExtra
      if (this.selectedCoupon) {
        total = Math.max(0, total - this.selectedCoupon.amount)
      }
      return total.toFixed(2)
    },
    canSubmit() {
      return this.endLocation.trim() !== ''
    },
    buttonText() {
      if (this.hasDriverAccepted) {
        return '行程进行中...'
      }
      if (this.orderMode === 'immediate') {
        return '立即叫代驾'
      }
      return '确认预约'
    }
  },
  watch: {
    selectedService(newVal) {
      if (newVal === 'business') {
        this.serviceExtra = 15
      } else if (newVal === 'vip') {
        this.serviceExtra = 30
      } else {
        this.serviceExtra = 0
      }
    }
  },
  onUnload() {
    if (this.waitingTimer) {
      clearInterval(this.waitingTimer)
    }
  },
  methods: {
    useCurrentLocation() {
      uni.showToast({ title: '已定位当前位置', icon: 'success' })
      this.startLocation = '北京市朝阳区建国路88号SOHO现代城'
    },
    clearDestination() {
      this.endLocation = ''
    },
    onDateChange(e) {
      this.reserveDate = e.detail.value
    },
    onTimeChange(e) {
      this.reserveTime = e.detail.value
    },
    selectCoupon() {
      uni.showActionSheet({
        itemList: ['20元优惠券（满80可用）', '30元优惠券（满120可用）', '不使用优惠券'],
        success: (res) => {
          if (res.tapIndex === 0) {
            this.selectedCoupon = { amount: 20 }
          } else if (res.tapIndex === 1) {
            this.selectedCoupon = { amount: 30 }
          } else {
            this.selectedCoupon = null
          }
        }
      })
    },
    submitOrder() {
      if (!this.canSubmit) {
        uni.showToast({ title: '请输入目的地', icon: 'none' })
        return
      }

      uni.showLoading({ title: '下单中...' })

      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({ title: '下单成功', icon: 'success' })

        setTimeout(() => {
          this.hasDriverAccepted = true
          this.startWaitingTimer()
          uni.showModal({
            title: '接单提醒',
            content: '张师傅已接单，正在赶来的路上',
            showCancel: false
          })
        }, 2000)
      }, 1500)
    },
    startWaitingTimer() {
      let seconds = 0
      this.waitingTimer = setInterval(() => {
        seconds++
        const mins = Math.floor(seconds / 60).toString().padStart(2, '0')
        const secs = (seconds % 60).toString().padStart(2, '0')
        this.waitingTime = `${mins}:${secs}`
      }, 1000)
    },
    callDriver() {
      uni.makePhoneCall({
        phoneNumber: '13888888888'
      })
    },
    goToEmergency() {
      uni.navigateTo({
        url: '/pages/emergency/emergency'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.order-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 140rpx;
}

.map-section {
  position: relative;
  height: 400rpx;
  background: linear-gradient(180deg, #E3F2FD 0%, #BBDEFB 100%);
}

.map-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.map-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.map-text {
  font-size: 28rpx;
  color: $text-muted;
}

.location-panel {
  position: absolute;
  bottom: 30rpx;
  left: 30rpx;
  right: 30rpx;
  background: $white;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: $shadow-lg;
}

.location-item {
  display: flex;
  align-items: center;
}

.location-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.start-dot {
  background: $success-color;
}

.end-dot {
  background: $danger-color;
}

.location-content {
  flex: 1;
}

.location-label {
  display: block;
  font-size: 24rpx;
  color: $text-muted;
  margin-bottom: 8rpx;
}

.location-input {
  font-size: 30rpx;
  color: $text-color;
}

.location-actions {
  padding: 10rpx;
}

.action-icon {
  font-size: 36rpx;
}

.location-divider {
  height: 30rpx;
  border-left: 2rpx dashed $border-color;
  margin: 10rpx 0 10rpx 29rpx;
}

.order-options {
  padding: 20rpx;
}

.option-section {
  background: $white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.title-icon {
  font-size: 36rpx;
  margin-right: 12rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: $text-color;
}

.mode-options {
  display: flex;
  gap: 20rpx;
}

.mode-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  padding: 24rpx;
  border: 2rpx solid $border-color;
  border-radius: 16rpx;
  position: relative;

  &.active {
    border-color: $primary-color;
    background: rgba(79, 195, 247, 0.05);
  }
}

.mode-icon {
  font-size: 40rpx;
  margin-right: 12rpx;
}

.mode-name {
  font-size: 28rpx;
  color: $text-color;
}

.mode-check {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  width: 32rpx;
  height: 32rpx;
  background: $primary-color;
  border-radius: 50%;
  color: $white;
  font-size: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.datetime-section {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid $border-color;
}

.datetime-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
}

.datetime-label {
  font-size: 28rpx;
  color: $text-color;
}

.datetime-value {
  font-size: 28rpx;
  color: $primary-color;
}

.datetime-arrow {
  font-size: 32rpx;
  color: $text-muted;
}

.car-options {
  display: flex;
  gap: 20rpx;
}

.car-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx;
  border: 2rpx solid $border-color;
  border-radius: 16rpx;

  &.active {
    border-color: $primary-color;
    background: rgba(79, 195, 247, 0.05);
  }
}

.car-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.car-name {
  font-size: 26rpx;
  color: $text-color;
  margin-bottom: 8rpx;
}

.car-price {
  font-size: 24rpx;
  color: $primary-color;
}

.service-options {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.service-item {
  width: calc(50% - 10rpx);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx;
  border: 2rpx solid $border-color;
  border-radius: 16rpx;

  &.active {
    border-color: $primary-color;
    background: rgba(79, 195, 247, 0.05);
  }
}

.service-icon-box {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.service-icon {
  font-size: 40rpx;
}

.service-name {
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 6rpx;
}

.service-desc {
  font-size: 22rpx;
  color: $text-muted;
}

.estimate-section {
  background: $white;
  border-radius: 20rpx;
  overflow: hidden;
}

.estimate-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.estimate-title {
  font-size: 30rpx;
  font-weight: 600;
  color: $primary-color;
}

.estimate-info {
  display: flex;
  gap: 20rpx;
}

.estimate-distance,
.estimate-time {
  font-size: 24rpx;
  color: $primary-dark;
}

.estimate-breakdown {
  padding: 0 30rpx;
}

.breakdown-item {
  display: flex;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;

  &.total {
    border-bottom: none;
    padding-top: 30rpx;
    padding-bottom: 0;
  }
}

.breakdown-label {
  font-size: 28rpx;
  color: $text-color;
}

.breakdown-value {
  font-size: 28rpx;
  color: $text-color;
}

.breakdown-total {
  font-size: 36rpx;
  font-weight: 600;
  color: $primary-color;
}

.coupon-bar {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  margin-top: 20rpx;
  background: linear-gradient(135deg, #FFF8E1 0%, #FFECB3 100%);
}

.coupon-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}

.coupon-text {
  flex: 1;
  font-size: 28rpx;
  color: $text-color;
}

.coupon-placeholder {
  color: $text-muted;
}

.coupon-arrow {
  font-size: 32rpx;
  color: $text-muted;
}

.driver-section {
  padding: 20rpx;
}

.driver-status-bar {
  display: flex;
  align-items: center;
  background: $white;
  border-radius: 16rpx;
  padding: 24rpx 30rpx;
  margin-bottom: 20rpx;
}

.status-dot {
  width: 16rpx;
  height: 16rpx;
  background: $success-color;
  border-radius: 50%;
  margin-right: 16rpx;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.status-text {
  flex: 1;
  font-size: 30rpx;
  font-weight: 600;
  color: $success-color;
}

.status-timer {
  font-size: 28rpx;
  color: $text-muted;
  font-family: monospace;
}

.driver-card {
  background: $white;
  border-radius: 20rpx;
  padding: 30rpx;
}

.driver-header {
  display: flex;
  align-items: center;
  padding-bottom: 24rpx;
  border-bottom: 1rpx solid $border-color;
}

.driver-avatar {
  width: 120rpx;
  height: 120rpx;
  background: linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-icon {
  font-size: 60rpx;
}

.driver-info {
  flex: 1;
}

.driver-name-row {
  display: flex;
  align-items: center;
  margin-bottom: 10rpx;
}

.driver-name {
  font-size: 32rpx;
  font-weight: 600;
  color: $text-color;
  margin-right: 16rpx;
}

.driver-tag {
  display: flex;
  align-items: center;
  background: #FFF3E0;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.tag-icon {
  font-size: 24rpx;
  margin-right: 4rpx;
}

.tag-text {
  font-size: 22rpx;
  color: $warning-color;
}

.driver-phone {
  font-size: 26rpx;
  color: $text-muted;
  margin-bottom: 10rpx;
}

.driver-stats {
  display: flex;
  align-items: center;
}

.stat-item {
  font-size: 24rpx;
  color: $text-muted;
}

.stat-divider {
  margin: 0 16rpx;
  color: $border-color;
}

.driver-contact {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 20rpx;
  background: rgba(79, 195, 247, 0.1);
  border-radius: 12rpx;
}

.contact-icon {
  font-size: 40rpx;
  margin-bottom: 6rpx;
}

.contact-text {
  font-size: 22rpx;
  color: $primary-color;
}

.driver-car-info {
  display: flex;
  gap: 40rpx;
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.car-info-item {
  display: flex;
  align-items: center;
}

.car-info-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
}

.car-info-text {
  font-size: 26rpx;
  color: $text-color;
}

.driver-location {
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.location-label {
  display: block;
  font-size: 24rpx;
  color: $text-muted;
  margin-bottom: 8rpx;
}

.location-value {
  display: block;
  font-size: 28rpx;
  color: $text-color;
}

.trip-info {
  padding-top: 24rpx;
}

.trip-item {
  display: flex;
  align-items: center;
  padding: 12rpx 0;
}

.trip-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.trip-dot.start {
  background: $success-color;
}

.trip-dot.end {
  background: $danger-color;
}

.trip-text {
  font-size: 28rpx;
  color: $text-color;
}

.trip-line {
  width: 2rpx;
  height: 40rpx;
  background: $border-color;
  margin-left: 7rpx;
}

.emergency-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  border-radius: 16rpx;
  padding: 24rpx;
  margin-top: 20rpx;
}

.emergency-icon {
  font-size: 40rpx;
  margin-right: 12rpx;
}

.emergency-text {
  font-size: 30rpx;
  font-weight: 600;
  color: $danger-color;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: $white;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  display: flex;
  align-items: center;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.price-info {
  display: flex;
  align-items: baseline;
  margin-right: 30rpx;
}

.price-label {
  font-size: 26rpx;
  color: $text-muted;
  margin-right: 10rpx;
}

.price-value {
  font-size: 40rpx;
  font-weight: 600;
  color: $primary-color;
}

.price-unit {
  font-size: 24rpx;
  color: $text-muted;
}

.submit-btn {
  flex: 1;
  background: $primary-color;
  color: $white;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  padding: 24rpx;
  border-radius: 50rpx;

  &.disabled {
    background: $border-color;
    color: $text-muted;
  }
}
</style>
