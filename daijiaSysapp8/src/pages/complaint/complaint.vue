<template>
  <view class="complaint-container">
    <view class="tab-bar">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === tab.value }" 
        v-for="tab in tabs" 
        :key="tab.value"
        @click="currentTab = tab.value"
      >
        <text class="tab-text">{{ tab.label }}</text>
        <view class="tab-badge" v-if="getBadgeCount(tab.value) > 0">{{ getBadgeCount(tab.value) }}</view>
      </view>
    </view>

    <view class="complaint-list">
      <view class="empty-state" v-if="filteredComplaints.length === 0">
        <text class="empty-icon">📝</text>
        <text class="empty-text">暂无{{ currentTab === 'all' ? '' : statusMap[currentTab] }}投诉单</text>
      </view>

      <view class="complaint-card" v-for="(complaint, index) in filteredComplaints" :key="index">
        <view class="card-header">
          <view class="order-info">
            <text class="order-label">投诉单号</text>
            <text class="order-value">{{ complaint.complaintNo }}</text>
          </view>
          <view class="status-tag" :class="complaint.status">
            {{ statusMap[complaint.status] }}
          </view>
        </view>

        <view class="card-content">
          <view class="complaint-type">
            <text class="type-icon">📌</text>
            <text class="type-text">{{ complaint.type }}</text>
          </view>
          <view class="complaint-desc">
            <text class="desc-label">投诉内容：</text>
            <text class="desc-text">{{ complaint.content }}</text>
          </view>
          <view class="complaint-time">
            <text class="time-label">提交时间：</text>
            <text class="time-value">{{ complaint.submitTime }}</text>
          </view>
        </view>

        <view class="result-section" v-if="complaint.status === 'resolved' || complaint.status === 'processing'">
          <view class="result-header">
            <text class="result-icon">💬</text>
            <text class="result-label">{{ complaint.status === 'processing' ? '处理进度' : '处理结果' }}</text>
          </view>
          <view class="result-content">
            <text class="result-text">{{ complaint.processNote || '正在处理中，请耐心等待...' }}</text>
            <text class="result-time" v-if="complaint.processTime">处理时间：{{ complaint.processTime }}</text>
          </view>
        </view>

        <view class="card-actions">
          <view class="action-btn outline" @click="viewDetail(complaint)" v-if="complaint.status !== 'resolved'">
            联系客服
          </view>
          <view class="action-btn outline" @click="viewDetail(complaint)">
            查看详情
          </view>
          <view class="action-btn primary" @click="cancelComplaint(complaint)" v-if="complaint.status === 'pending'">
            撤销投诉
          </view>
        </view>
      </view>
    </view>

    <view class="floating-btn" @click="goToCreateComplaint">
      <text class="btn-icon">+</text>
      <text class="btn-text">我要投诉</text>
    </view>

    <view class="create-modal" v-if="showCreateModal" @click="closeCreateModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">提交投诉</text>
          <text class="modal-close" @click="closeCreateModal">✕</text>
        </view>

        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">选择订单</text>
            <picker :range="orderList" range-key="orderNo" @change="onOrderChange">
              <view class="form-picker">
                <text class="picker-value" v-if="createForm.orderId">{{ selectedOrder.orderNo }}</text>
                <text class="picker-placeholder" v-else>请选择投诉订单</text>
                <text class="picker-arrow">›</text>
              </view>
            </picker>
          </view>

          <view class="form-item">
            <text class="form-label">投诉类型</text>
            <view class="type-options">
              <view 
                class="type-option" 
                :class="{ active: createForm.type === type.value }"
                v-for="type in complaintTypes" 
                :key="type.value"
                @click="createForm.type = type.value"
              >
                <text class="type-option-text">{{ type.label }}</text>
              </view>
            </view>
          </view>

          <view class="form-item">
            <text class="form-label">投诉内容</text>
            <textarea 
              class="form-textarea" 
              placeholder="请详细描述您遇到的问题..."
              v-model="createForm.content"
              maxlength="500"
            ></textarea>
            <text class="char-count">{{ createForm.content.length }}/500</text>
          </view>

          <view class="form-item">
            <text class="form-label">上传凭证（选填）</text>
            <view class="image-list">
              <view class="image-item" v-for="(img, index) in createForm.images" :key="index">
                <image class="image-preview" :src="img" mode="aspectFill"></image>
                <view class="image-remove" @click="removeImage(index)">✕</view>
              </view>
              <view class="image-add" v-if="createForm.images.length < 9" @click="addImage">
                <text class="add-icon">+</text>
                <text class="add-text">添加图片</text>
              </view>
            </view>
          </view>

          <view class="form-item">
            <text class="form-label">联系电话</text>
            <input 
              class="form-input" 
              type="number" 
              placeholder="请输入联系电话"
              v-model="createForm.phone"
              maxlength="11"
            />
          </view>
        </view>

        <view class="modal-footer">
          <view class="btn-cancel" @click="closeCreateModal">取消</view>
          <view class="btn-submit" @click="submitComplaint">提交投诉</view>
        </view>
      </view>
    </view>

    <view class="detail-modal" v-if="showDetailModal" @click="closeDetailModal">
      <view class="modal-content detail-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">投诉详情</text>
          <text class="modal-close" @click="closeDetailModal">✕</text>
        </view>

        <view class="modal-body">
          <view class="detail-item">
            <text class="detail-label">投诉单号</text>
            <text class="detail-value">{{ currentComplaint.complaintNo }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">关联订单</text>
            <text class="detail-value">{{ currentComplaint.orderNo }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">投诉类型</text>
            <text class="detail-value">{{ currentComplaint.type }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">当前状态</text>
            <view class="status-tag" :class="currentComplaint.status">
              {{ statusMap[currentComplaint.status] }}
            </view>
          </view>
          <view class="detail-item full">
            <text class="detail-label">投诉内容</text>
            <text class="detail-text">{{ currentComplaint.content }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">提交时间</text>
            <text class="detail-value">{{ currentComplaint.submitTime }}</text>
          </view>
          <view class="detail-item full" v-if="currentComplaint.processNote">
            <text class="detail-label">处理结果</text>
            <text class="detail-text">{{ currentComplaint.processNote }}</text>
          </view>
          <view class="detail-item" v-if="currentComplaint.processTime">
            <text class="detail-label">处理时间</text>
            <text class="detail-value">{{ currentComplaint.processTime }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'all',
      tabs: [
        { label: '全部', value: 'all' },
        { label: '待处理', value: 'pending' },
        { label: '处理中', value: 'processing' },
        { label: '已解决', value: 'resolved' }
      ],
      statusMap: {
        pending: '待处理',
        processing: '处理中',
        resolved: '已解决'
      },
      complaints: [
        {
          id: 1,
          complaintNo: 'TS202605020001',
          orderNo: 'DJ202605021830001',
          type: '服务态度',
          content: '司机服务态度不好，途中多次接打电话，让我感到很不安全。',
          submitTime: '2026-05-02 19:30:15',
          status: 'pending',
          processNote: '',
          processTime: ''
        },
        {
          id: 2,
          complaintNo: 'TS202605010002',
          orderNo: 'DJ202605011900002',
          type: '费用问题',
          content: '实际行驶里程与预估不符，费用比预估高出很多，希望能核实并退还多收的费用。',
          submitTime: '2026-05-01 21:15:30',
          status: 'processing',
          processNote: '您好，您的投诉已收到，我们正在核实订单详情和行驶路线，预计24小时内给您回复。',
          processTime: '2026-05-01 22:00:00'
        },
        {
          id: 3,
          complaintNo: 'TS202604280001',
          orderNo: 'DJ202604282030003',
          type: '安全问题',
          content: '司机驾车时存在危险驾驶行为，多次违规变道，建议加强司机安全培训。',
          submitTime: '2026-04-28 22:00:15',
          status: 'resolved',
          processNote: '尊敬的用户，非常抱歉给您带来了不好的体验。我们已对该司机进行了安全培训和考核，并给予了相应处罚。同时为您补偿一张50元代驾券，已发放至您的账户，请注意查收。',
          processTime: '2026-04-29 10:30:00'
        }
      ],
      showCreateModal: false,
      showDetailModal: false,
      currentComplaint: {
        id: '',
        complaintNo: '',
        orderNo: '',
        type: '',
        content: '',
        submitTime: '',
        status: '',
        processNote: '',
        processTime: ''
      },
      createForm: {
        orderId: '',
        type: '',
        content: '',
        images: [],
        phone: ''
      },
      complaintTypes: [
        { label: '服务态度', value: 'service' },
        { label: '费用问题', value: 'fee' },
        { label: '安全问题', value: 'safety' },
        { label: '其他问题', value: 'other' }
      ],
      orderList: [
        { id: 1, orderNo: 'DJ202605021830001' },
        { id: 2, orderNo: 'DJ202605011900002' },
        { id: 3, orderNo: 'DJ202604282030003' }
      ],
      selectedOrder: null
    }
  },
  computed: {
    filteredComplaints() {
      if (this.currentTab === 'all') {
        return this.complaints
      }
      return this.complaints.filter(item => item.status === this.currentTab)
    }
  },
  methods: {
    getBadgeCount(tab) {
      if (tab === 'pending') {
        return this.complaints.filter(item => item.status === 'pending').length
      }
      if (tab === 'processing') {
        return this.complaints.filter(item => item.status === 'processing').length
      }
      return 0
    },

    goToCreateComplaint() {
      this.resetCreateForm()
      this.showCreateModal = true
    },

    resetCreateForm() {
      this.createForm = {
        orderId: '',
        type: '',
        content: '',
        images: [],
        phone: ''
      }
      this.selectedOrder = null
    },

    closeCreateModal() {
      this.showCreateModal = false
    },

    onOrderChange(e) {
      const index = e.detail.value
      this.createForm.orderId = this.orderList[index].id
      this.selectedOrder = this.orderList[index]
    },

    addImage() {
      uni.chooseImage({
        count: 9 - this.createForm.images.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.createForm.images = [...this.createForm.images, ...res.tempFilePaths]
        }
      })
    },

    removeImage(index) {
      this.createForm.images.splice(index, 1)
    },

    submitComplaint() {
      if (!this.createForm.orderId) {
        uni.showToast({ title: '请选择投诉订单', icon: 'none' })
        return
      }
      if (!this.createForm.type) {
        uni.showToast({ title: '请选择投诉类型', icon: 'none' })
        return
      }
      if (!this.createForm.content.trim()) {
        uni.showToast({ title: '请输入投诉内容', icon: 'none' })
        return
      }

      uni.showLoading({ title: '提交中...' })

      setTimeout(() => {
        uni.hideLoading()
        
        const typeLabel = this.complaintTypes.find(t => t.value === this.createForm.type)?.label || '其他问题'
        const newComplaint = {
          id: Date.now(),
          complaintNo: `TS${this.formatDate(new Date())}${String(this.complaints.length + 1).padStart(4, '0')}`,
          orderNo: this.selectedOrder.orderNo,
          type: typeLabel,
          content: this.createForm.content,
          submitTime: this.formatTime(new Date()),
          status: 'pending',
          processNote: '',
          processTime: ''
        }

        this.complaints.unshift(newComplaint)
        this.closeCreateModal()
        uni.showToast({ title: '投诉已提交', icon: 'success' })
      }, 1500)
    },

    viewDetail(complaint) {
      this.currentComplaint = { ...complaint }
      this.showDetailModal = true
    },

    closeDetailModal() {
      this.showDetailModal = false
    },

    cancelComplaint(complaint) {
      uni.showModal({
        title: '撤销投诉',
        content: '确定要撤销该投诉吗？撤销后将无法恢复。',
        success: (res) => {
          if (res.confirm) {
            const index = this.complaints.findIndex(item => item.id === complaint.id)
            if (index > -1) {
              this.complaints.splice(index, 1)
            }
            uni.showToast({ title: '已撤销', icon: 'success' })
          }
        }
      })
    },

    formatDate(date) {
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      return `${year}${month}${day}`
    },

    formatTime(date) {
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hour = date.getHours().toString().padStart(2, '0')
      const minute = date.getMinutes().toString().padStart(2, '0')
      const second = date.getSeconds().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 140rpx;
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
  font-size: 28rpx;
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

.complaint-list {
  padding: 20rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
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

.complaint-card {
  background: $white;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.order-info {
  display: flex;
  align-items: center;
}

.order-label {
  font-size: 24rpx;
  color: $text-muted;
  margin-right: 10rpx;
}

.order-value {
  font-size: 26rpx;
  font-weight: 500;
  color: $primary-color;
}

.status-tag {
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;

  &.pending {
    background: #FFF3E0;
    color: $warning-color;
  }

  &.processing {
    background: #E3F2FD;
    color: $primary-color;
  }

  &.resolved {
    background: #E8F5E9;
    color: $success-color;
  }
}

.card-content {
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid $border-color;
}

.complaint-type {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.type-icon {
  font-size: 28rpx;
  margin-right: 10rpx;
}

.type-text {
  font-size: 26rpx;
  color: $primary-color;
  background: rgba(79, 195, 247, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.complaint-desc {
  margin-bottom: 12rpx;
}

.desc-label,
.time-label {
  font-size: 26rpx;
  color: $text-muted;
}

.desc-text,
.time-value {
  font-size: 26rpx;
  color: $text-color;
}

.complaint-time {
  margin-top: 12rpx;
}

.result-section {
  padding: 24rpx 30rpx;
  background: #F5F5F5;
  border-bottom: 1rpx solid $border-color;
}

.result-header {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.result-icon {
  font-size: 28rpx;
  margin-right: 10rpx;
}

.result-label {
  font-size: 26rpx;
  font-weight: 500;
  color: $text-color;
}

.result-content {
  padding-left: 38rpx;
}

.result-text {
  font-size: 26rpx;
  color: $text-color;
  line-height: 1.6;
  display: block;
}

.result-time {
  font-size: 24rpx;
  color: $text-muted;
  margin-top: 10rpx;
  display: block;
}

.card-actions {
  display: flex;
  gap: 20rpx;
  padding: 24rpx 30rpx;
}

.action-btn {
  flex: 1;
  text-align: center;
  font-size: 28rpx;
  padding: 18rpx 0;
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

.floating-btn {
  position: fixed;
  right: 30rpx;
  bottom: calc(30rpx + env(safe-area-inset-bottom));
  display: flex;
  align-items: center;
  background: $primary-color;
  color: $white;
  padding: 20rpx 30rpx;
  border-radius: 50rpx;
  box-shadow: 0 8rpx 24rpx rgba(79, 195, 247, 0.4);
  z-index: 100;
}

.btn-icon {
  font-size: 36rpx;
  font-weight: 300;
  margin-right: 10rpx;
}

.btn-text {
  font-size: 28rpx;
  font-weight: 500;
}

.create-modal,
.detail-modal {
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

.detail-modal {
  align-items: center;
}

.modal-content {
  width: 100%;
  background: $white;
  border-radius: 32rpx 32rpx 0 0;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.detail-content {
  width: 90%;
  border-radius: 24rpx;
  max-height: 80vh;
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

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 16rpx;
}

.form-picker {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: $background-color;
  border-radius: 12rpx;
  padding: 24rpx;
}

.picker-value {
  font-size: 28rpx;
  color: $text-color;
}

.picker-placeholder {
  font-size: 28rpx;
  color: $text-muted;
}

.picker-arrow {
  font-size: 32rpx;
  color: $text-muted;
}

.type-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.type-option {
  padding: 16rpx 28rpx;
  background: $background-color;
  border-radius: 50rpx;
  border: 2rpx solid transparent;

  &.active {
    background: rgba(79, 195, 247, 0.1);
    border-color: $primary-color;
  }
}

.type-option-text {
  font-size: 26rpx;
  color: $text-color;

  .active & {
    color: $primary-color;
  }
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  background: $background-color;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.char-count {
  display: block;
  text-align: right;
  font-size: 24rpx;
  color: $text-muted;
  margin-top: 10rpx;
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

.form-input {
  width: 100%;
  background: $background-color;
  border-radius: 12rpx;
  padding: 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
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

.detail-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid $border-color;

  &.full {
    flex-direction: column;
  }

  &:last-child {
    border-bottom: none;
  }
}

.detail-label {
  font-size: 28rpx;
  color: $text-muted;
  min-width: 180rpx;
  margin-bottom: 10rpx;
}

.detail-value {
  font-size: 28rpx;
  color: $text-color;
  flex: 1;
}

.detail-text {
  font-size: 28rpx;
  color: $text-color;
  line-height: 1.6;
}
</style>
