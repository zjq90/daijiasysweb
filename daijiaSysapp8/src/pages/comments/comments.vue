<template>
  <view class="comments-container">
    <view class="tab-bar">
      <view class="tab-item" :class="{ active: currentTab === 'pending' }" @click="currentTab = 'pending'">
        <text class="tab-text">待评价</text>
        <view class="tab-badge" v-if="pendingList.length > 0">{{ pendingList.length }}</view>
      </view>
      <view class="tab-item" :class="{ active: currentTab === 'history' }" @click="currentTab = 'history'">
        <text class="tab-text">历史评价</text>
      </view>
    </view>

    <view class="pending-section" v-if="currentTab === 'pending'">
      <view class="empty-state" v-if="pendingList.length === 0">
        <text class="empty-icon">📝</text>
        <text class="empty-text">暂无待评价订单</text>
      </view>
      
      <view class="order-card" v-for="(order, index) in pendingList" :key="index">
        <view class="order-header">
          <view class="order-info">
            <text class="order-type">{{ order.type === 'immediate' ? '立即代驾' : '预约代驾' }}</text>
            <text class="order-time">{{ order.orderTime }}</text>
          </view>
          <text class="order-status">待评价</text>
        </view>

        <view class="order-location">
          <view class="location-row">
            <view class="location-dot start"></view>
            <text class="location-text">{{ order.startLocation }}</text>
          </view>
          <view class="location-line"></view>
          <view class="location-row">
            <view class="location-dot end"></view>
            <text class="location-text">{{ order.endLocation }}</text>
          </view>
        </view>

        <view class="order-summary">
          <view class="summary-item">
            <text class="summary-label">司机</text>
            <text class="summary-value">{{ order.driverName }}</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">里程</text>
            <text class="summary-value">{{ order.distance }}公里</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">费用</text>
            <text class="summary-value price">¥{{ order.price }}</text>
          </view>
        </view>

        <view class="order-actions">
          <view class="action-btn outline" @click="viewDriverInfo(order)">
            查看司机
          </view>
          <view class="action-btn primary" @click="goToComment(order)">
            立即评价
          </view>
        </view>
      </view>
    </view>

    <view class="history-section" v-else>
      <view class="empty-state" v-if="historyList.length === 0">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无历史评价</text>
      </view>

      <view class="comment-card" v-for="(comment, index) in historyList" :key="index">
        <view class="comment-header">
          <view class="driver-info">
            <view class="driver-avatar">
              <text class="avatar-icon">👨‍✈️</text>
            </view>
            <view class="driver-content">
              <text class="driver-name">{{ comment.driverName }}</text>
              <view class="comment-stars">
                <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= comment.rating }">★</text>
              </view>
            </view>
          </view>
          <text class="comment-time">{{ comment.commentTime }}</text>
        </view>

        <view class="comment-detail">
          <view class="detail-item">
            <text class="detail-label">驾驶技术</text>
            <view class="detail-stars">
              <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= comment.drivingSkill }">★</text>
            </view>
          </view>
          <view class="detail-item">
            <text class="detail-label">服务态度</text>
            <view class="detail-stars">
              <text class="star" v-for="i in 5" :key="i" :class="{ active: i <= comment.serviceAttitude }">★</text>
            </view>
          </view>
        </view>

        <view class="comment-content" v-if="comment.content">
          <text class="content-text">{{ comment.content }}</text>
        </view>

        <view class="comment-images" v-if="comment.images && comment.images.length > 0">
          <image 
            class="comment-image" 
            v-for="(img, imgIndex) in comment.images" 
            :key="imgIndex"
            :src="img"
            mode="aspectFill"
          ></image>
        </view>
      </view>
    </view>

    <view class="comment-modal" v-if="showCommentModal" @click="closeCommentModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">评价司机</text>
          <text class="modal-close" @click="closeCommentModal">✕</text>
        </view>

        <view class="modal-body">
          <view class="score-section">
            <text class="score-label">整体评分</text>
            <view class="score-stars">
              <text 
                class="star" 
                v-for="i in 5" 
                :key="i" 
                :class="{ active: i <= commentForm.rating }"
                @click="commentForm.rating = i"
              >★</text>
            </view>
            <text class="score-tip">{{ scoreTip }}</text>
          </view>

          <view class="detail-score-section">
            <view class="detail-score-item">
              <text class="detail-score-label">驾驶技术</text>
              <view class="detail-score-stars">
                <text 
                  class="star" 
                  v-for="i in 5" 
                  :key="i" 
                  :class="{ active: i <= commentForm.drivingSkill }"
                  @click="commentForm.drivingSkill = i"
                >★</text>
              </view>
            </view>
            <view class="detail-score-item">
              <text class="detail-score-label">服务态度</text>
              <view class="detail-score-stars">
                <text 
                  class="star" 
                  v-for="i in 5" 
                  :key="i" 
                  :class="{ active: i <= commentForm.serviceAttitude }"
                  @click="commentForm.serviceAttitude = i"
                >★</text>
              </view>
            </view>
          </view>

          <view class="content-section">
            <text class="content-label">评价内容</text>
            <textarea 
              class="content-input" 
              placeholder="请输入您的评价内容（选填）"
              v-model="commentForm.content"
              maxlength="500"
            ></textarea>
            <text class="content-count">{{ commentForm.content.length }}/500</text>
          </view>

          <view class="image-section">
            <text class="image-label">添加图片</text>
            <view class="image-list">
              <view class="image-item" v-for="(img, index) in commentForm.images" :key="index">
                <image class="image-preview" :src="img" mode="aspectFill"></image>
                <view class="image-remove" @click="removeImage(index)">✕</view>
              </view>
              <view class="image-add" v-if="commentForm.images.length < 9" @click="addImage">
                <text class="add-icon">+</text>
                <text class="add-text">添加图片</text>
              </view>
            </view>
          </view>
        </view>

        <view class="modal-footer">
          <view class="btn-cancel" @click="closeCommentModal">取消</view>
          <view class="btn-submit" @click="submitComment">提交评价</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'pending',
      pendingList: [
        {
          id: 1,
          type: 'immediate',
          orderTime: '2026-05-02 18:30',
          startLocation: '北京市朝阳区建国路88号SOHO现代城',
          endLocation: '北京市海淀区中关村软件园',
          driverName: '张师傅',
          distance: 15.6,
          price: 85.50
        },
        {
          id: 2,
          type: 'reserve',
          orderTime: '2026-05-01 19:00',
          startLocation: '北京市朝阳区三里屯',
          endLocation: '北京市通州区新华大街',
          driverName: '李师傅',
          distance: 22.3,
          price: 120.00
        }
      ],
      historyList: [
        {
          id: 1,
          driverName: '王师傅',
          commentTime: '2026-04-28 20:30',
          rating: 5,
          drivingSkill: 5,
          serviceAttitude: 5,
          content: '师傅驾驶技术很好，服务态度也很好，全程平稳，下次还会选择这家代驾。',
          images: []
        },
        {
          id: 2,
          driverName: '赵师傅',
          commentTime: '2026-04-25 21:15',
          rating: 4,
          drivingSkill: 4,
          serviceAttitude: 5,
          content: '师傅准时到达，服务态度很好，驾驶也比较平稳。',
          images: []
        }
      ],
      showCommentModal: false,
      currentOrder: null,
      commentForm: {
        rating: 5,
        drivingSkill: 5,
        serviceAttitude: 5,
        content: '',
        images: []
      }
    }
  },
  computed: {
    scoreTip() {
      const tips = ['', '非常差', '较差', '一般', '较好', '非常好']
      return tips[this.commentForm.rating] || ''
    }
  },
  methods: {
    viewDriverInfo(order) {
      uni.navigateTo({
        url: '/pages/driver-info/driver-info?id=' + order.id
      })
    },

    goToComment(order) {
      this.currentOrder = order
      this.resetCommentForm()
      this.showCommentModal = true
    },

    resetCommentForm() {
      this.commentForm = {
        rating: 5,
        drivingSkill: 5,
        serviceAttitude: 5,
        content: '',
        images: []
      }
    },

    closeCommentModal() {
      this.showCommentModal = false
    },

    addImage() {
      uni.chooseImage({
        count: 9 - this.commentForm.images.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.commentForm.images = [...this.commentForm.images, ...res.tempFilePaths]
        }
      })
    },

    removeImage(index) {
      this.commentForm.images.splice(index, 1)
    },

    submitComment() {
      if (this.commentForm.rating === 0) {
        uni.showToast({ title: '请选择评分', icon: 'none' })
        return
      }

      uni.showLoading({ title: '提交中...' })

      setTimeout(() => {
        uni.hideLoading()
        
        const newComment = {
          id: Date.now(),
          driverName: this.currentOrder.driverName,
          commentTime: this.formatTime(new Date()),
          rating: this.commentForm.rating,
          drivingSkill: this.commentForm.drivingSkill,
          serviceAttitude: this.commentForm.serviceAttitude,
          content: this.commentForm.content,
          images: this.commentForm.images
        }

        this.historyList.unshift(newComment)

        const index = this.pendingList.findIndex(item => item.id === this.currentOrder.id)
        if (index > -1) {
          this.pendingList.splice(index, 1)
        }

        this.closeCommentModal()
        uni.showToast({ title: '评价成功', icon: 'success' })
      }, 1500)
    },

    formatTime(date) {
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hour = date.getHours().toString().padStart(2, '0')
      const minute = date.getMinutes().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    }
  }
}
</script>

<style lang="scss" scoped>
.comments-container {
  min-height: 100vh;
  background: $background-color;
}

.tab-bar {
  display: flex;
  background: $white;
  padding: 0 30rpx;
}

.tab-item {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx 0;

  &.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60rpx;
    height: 6rpx;
    background: $primary-color;
    border-radius: 3rpx;
  }
}

.tab-text {
  font-size: 30rpx;
  color: $text-muted;

  .active & {
    color: $primary-color;
    font-weight: 600;
  }
}

.tab-badge {
  background: $danger-color;
  color: $white;
  font-size: 20rpx;
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
  margin-left: 10rpx;
  min-width: 30rpx;
  text-align: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: $text-muted;
}

.order-card {
  background: $white;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid $border-color;
}

.order-info {
  display: flex;
  align-items: center;
}

.order-type {
  font-size: 26rpx;
  color: $primary-color;
  background: rgba(79, 195, 247, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  margin-right: 16rpx;
}

.order-time {
  font-size: 24rpx;
  color: $text-muted;
}

.order-status {
  font-size: 26rpx;
  color: $warning-color;
}

.order-location {
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.location-row {
  display: flex;
  align-items: center;
  padding: 10rpx 0;
}

.location-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.location-dot.start {
  background: $success-color;
}

.location-dot.end {
  background: $danger-color;
}

.location-text {
  flex: 1;
  font-size: 28rpx;
  color: $text-color;
}

.location-line {
  width: 2rpx;
  height: 30rpx;
  background: $border-color;
  margin-left: 7rpx;
}

.order-summary {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.summary-item {
  flex: 1;
  text-align: center;
}

.summary-label {
  display: block;
  font-size: 24rpx;
  color: $text-muted;
  margin-bottom: 8rpx;
}

.summary-value {
  font-size: 28rpx;
  color: $text-color;

  &.price {
    color: $primary-color;
    font-weight: 600;
  }
}

.order-actions {
  display: flex;
  gap: 20rpx;
  padding-top: 24rpx;
}

.action-btn {
  flex: 1;
  text-align: center;
  font-size: 28rpx;
  padding: 20rpx 0;
  border-radius: 50rpx;

  &.outline {
    color: $primary-color;
    border: 2rpx solid $primary-color;
  }

  &.primary {
    background: $primary-color;
    color: $white;
  }
}

.comment-card {
  background: $white;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.driver-info {
  display: flex;
  align-items: center;
}

.driver-avatar {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #4FC3F7 0%, #29B6F6 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-icon {
  font-size: 40rpx;
}

.driver-content {
  display: flex;
  flex-direction: column;
}

.driver-name {
  font-size: 30rpx;
  font-weight: 600;
  color: $text-color;
  margin-bottom: 8rpx;
}

.comment-stars {
  display: flex;
}

.star {
  font-size: 28rpx;
  color: $border-color;
  margin-right: 4rpx;

  &.active {
    color: #FFD700;
  }
}

.comment-time {
  font-size: 24rpx;
  color: $text-muted;
}

.comment-detail {
  padding: 20rpx 0;
  border-top: 1rpx solid $border-color;
  border-bottom: 1rpx solid $border-color;
  margin-bottom: 20rpx;
}

.detail-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10rpx 0;
}

.detail-label {
  font-size: 26rpx;
  color: $text-muted;
}

.detail-stars {
  display: flex;
}

.comment-content {
  margin-bottom: 20rpx;
}

.content-text {
  font-size: 28rpx;
  color: $text-color;
  line-height: 1.6;
}

.comment-images {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.comment-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
}

.comment-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-end;
  z-index: 1000;
}

.modal-content {
  width: 100%;
  background: $white;
  border-radius: 32rpx 32rpx 0 0;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid $border-color;
}

.modal-title {
  font-size: 32rpx;
  font-weight: 600;
  color: $text-color;
}

.modal-close {
  font-size: 36rpx;
  color: $text-muted;
}

.modal-body {
  flex: 1;
  overflow-y: auto;
  padding: 30rpx;
}

.score-section {
  text-align: center;
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid $border-color;
  margin-bottom: 30rpx;
}

.score-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 20rpx;
}

.score-stars {
  display: flex;
  justify-content: center;
  gap: 20rpx;
  margin-bottom: 16rpx;
}

.score-stars .star {
  font-size: 60rpx;
}

.score-tip {
  font-size: 26rpx;
  color: $primary-color;
}

.detail-score-section {
  margin-bottom: 30rpx;
}

.detail-score-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.detail-score-label {
  font-size: 28rpx;
  color: $text-color;
}

.detail-score-stars .star {
  font-size: 40rpx;
}

.content-section {
  margin-bottom: 30rpx;
}

.content-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 16rpx;
}

.content-input {
  width: 100%;
  height: 200rpx;
  background: $background-color;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.content-count {
  display: block;
  text-align: right;
  font-size: 24rpx;
  color: $text-muted;
  margin-top: 10rpx;
}

.image-section {
  margin-bottom: 30rpx;
}

.image-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 16rpx;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.image-item {
  position: relative;
  width: 160rpx;
  height: 160rpx;
}

.image-preview {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
}

.image-remove {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 40rpx;
  height: 40rpx;
  background: rgba(0, 0, 0, 0.6);
  color: $white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
}

.image-add {
  width: 160rpx;
  height: 160rpx;
  background: $background-color;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.add-icon {
  font-size: 60rpx;
  color: $text-muted;
}

.add-text {
  font-size: 24rpx;
  color: $text-muted;
  margin-top: 8rpx;
}

.modal-footer {
  display: flex;
  padding: 30rpx;
  padding-bottom: calc(30rpx + env(safe-area-inset-bottom));
  gap: 20rpx;
  border-top: 1rpx solid $border-color;
}

.btn-cancel {
  flex: 1;
  text-align: center;
  font-size: 30rpx;
  padding: 24rpx 0;
  border-radius: 50rpx;
  background: $background-color;
  color: $text-color;
}

.btn-submit {
  flex: 1;
  text-align: center;
  font-size: 30rpx;
  padding: 24rpx 0;
  border-radius: 50rpx;
  background: $primary-color;
  color: $white;
}
</style>
