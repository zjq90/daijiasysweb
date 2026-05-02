<template>
  <view class="emergency-container">
    <view class="warning-banner">
      <text class="warning-icon">⚠️</text>
      <text class="warning-text">如遇紧急情况，请立即拨打报警电话110</text>
    </view>

    <view class="quick-action-section">
      <view class="emergency-btn" @click="triggerEmergency">
        <view class="btn-inner">
          <text class="btn-icon">🆘</text>
          <text class="btn-text">紧急求助</text>
          <text class="btn-subtext">点击发送求助信息</text>
        </view>
      </view>
    </view>

    <view class="recording-section" v-if="isRecording || hasRecording">
      <view class="section-header">
        <text class="section-title">录音功能</text>
      </view>
      <view class="recording-card">
        <view class="recording-status" v-if="isRecording">
          <view class="status-dot"></view>
          <text class="status-text">正在录音中...</text>
          <text class="recording-time">{{ recordingTime }}</text>
        </view>
        <view class="recording-info" v-else-if="hasRecording">
          <view class="audio-preview">
            <text class="audio-icon">🎵</text>
            <view class="audio-progress">
              <view class="progress-bar">
                <view class="progress-fill"></view>
              </view>
              <view class="audio-time">
                <text>00:00</text>
                <text>{{ audioDuration }}</text>
              </view>
            </view>
            <view class="audio-controls">
              <view class="control-btn play" @click="playRecording">
                <text class="control-icon">{{ isPlaying ? '⏸️' : '▶️' }}</text>
              </view>
            </view>
          </view>
        </view>
        <view class="recording-actions">
          <view class="action-btn" :class="{ recording: isRecording }" @click="toggleRecording">
            <text class="action-icon">{{ isRecording ? '⏹️' : '🎙️' }}</text>
            <text class="action-text">{{ isRecording ? '停止录音' : '开始录音' }}</text>
          </view>
          <view class="action-btn" v-if="hasRecording && !isRecording" @click="saveRecording">
            <text class="action-icon">💾</text>
            <text class="action-text">保存录音</text>
          </view>
          <view class="action-btn" v-if="hasRecording && !isRecording" @click="deleteRecording">
            <text class="action-icon">🗑️</text>
            <text class="action-text">删除</text>
          </view>
        </view>
      </view>
    </view>

    <view class="record-section">
      <view class="section-header">
        <text class="section-title">求助记录</text>
        <text class="section-more" @click="goToAllRecords">全部记录 ›</text>
      </view>
      <view class="record-list">
        <view class="record-empty" v-if="records.length === 0">
          <text class="empty-icon">📋</text>
          <text class="empty-text">暂无求助记录</text>
        </view>
        <view class="record-item" v-for="(record, index) in records" :key="index">
          <view class="record-status" :class="record.status">
            <text class="status-icon" v-if="record.status === 'pending'">⏳</text>
            <text class="status-icon" v-else-if="record.status === 'processing'">🔄</text>
            <text class="status-icon" v-else>✅</text>
            <text class="status-text">{{ statusText[record.status] }}</text>
          </view>
          <view class="record-content">
            <view class="record-info">
              <text class="info-label">求助时间：</text>
              <text class="info-value">{{ record.time }}</text>
            </view>
            <view class="record-info">
              <text class="info-label">当前位置：</text>
              <text class="info-value">{{ record.location }}</text>
            </view>
            <view class="record-info" v-if="record.hasAudio">
              <text class="info-label">录音文件：</text>
              <text class="info-value audio-link" @click="playRecordAudio(record)">🎵 {{ record.audioName }}</text>
            </view>
          </view>
          <view class="record-detail" @click="viewRecordDetail(record)">
            <text class="detail-text">查看详情</text>
            <text class="detail-arrow">›</text>
          </view>
        </view>
      </view>
    </view>

    <view class="emergency-detail-modal" v-if="showDetailModal" @click="closeDetailModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">求助详情</text>
          <text class="modal-close" @click="closeDetailModal">✕</text>
        </view>
        <view class="modal-body">
          <view class="detail-item">
            <text class="detail-label">求助时间</text>
            <text class="detail-value">{{ currentRecord.time }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">当前状态</text>
            <text class="detail-value status" :class="currentRecord.status">{{ statusText[currentRecord.status] }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">求助位置</text>
            <text class="detail-value">{{ currentRecord.location }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">经纬度</text>
            <text class="detail-value">{{ currentRecord.latitude }}, {{ currentRecord.longitude }}</text>
          </view>
          <view class="detail-item" v-if="currentRecord.hasAudio">
            <text class="detail-label">录音文件</text>
            <view class="audio-player">
              <text class="audio-icon">🎵</text>
              <text class="audio-name">{{ currentRecord.audioName }}</text>
              <view class="audio-play-btn" @click="playCurrentRecordAudio">
                <text>{{ isPlaying ? '暂停' : '播放' }}</text>
              </view>
            </view>
          </view>
          <view class="detail-item" v-if="currentRecord.processNote">
            <text class="detail-label">处理说明</text>
            <text class="detail-value">{{ currentRecord.processNote }}</text>
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
      isRecording: false,
      hasRecording: false,
      isPlaying: false,
      recordingTime: '00:00',
      audioDuration: '01:23',
      recordingTimer: null,
      recordingSeconds: 0,
      records: [
        {
          id: 1,
          time: '2026-05-01 20:30:15',
          location: '北京市朝阳区建国路88号附近',
          latitude: '39.9042',
          longitude: '116.4074',
          status: 'completed',
          hasAudio: true,
          audioName: 'recording_20260501_203015.m4a',
          processNote: '已联系用户，情况已确认安全'
        },
        {
          id: 2,
          time: '2026-04-28 19:15:30',
          location: '北京市海淀区中关村软件园',
          latitude: '40.0478',
          longitude: '116.2829',
          status: 'processing',
          hasAudio: false,
          processNote: '正在处理中，已联系相关人员'
        }
      ],
      statusText: {
        pending: '待处理',
        processing: '处理中',
        completed: '已完成'
      },
      showDetailModal: false,
      currentRecord: {
        id: '',
        time: '',
        location: '',
        latitude: '',
        longitude: '',
        status: '',
        hasAudio: false,
        audioName: '',
        processNote: ''
      }
    }
  },
  onUnload() {
    if (this.recordingTimer) {
      clearInterval(this.recordingTimer)
    }
  },
  methods: {
    triggerEmergency() {
      uni.showModal({
        title: '紧急求助确认',
        content: '确定要发送紧急求助吗？系统将立即发送您的当前位置和行程信息给后台客服和紧急联系人。',
        confirmText: '确认求助',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            this.sendEmergencyHelp()
          }
        }
      })
    },

    sendEmergencyHelp() {
      uni.showLoading({ title: '发送求助中...' })

      setTimeout(() => {
        uni.hideLoading()
        
        const newRecord = {
          id: Date.now(),
          time: this.formatTime(new Date()),
          location: '北京市朝阳区建国路88号附近',
          latitude: '39.9042',
          longitude: '116.4074',
          status: 'pending',
          hasAudio: this.hasRecording,
          audioName: this.hasRecording ? `recording_${Date.now()}.m4a` : '',
          processNote: ''
        }

        this.records.unshift(newRecord)

        uni.showModal({
          title: '求助已发送',
          content: '您的求助信息已发送，客服人员将尽快与您联系。如遇危险请立即拨打110报警电话。',
          showCancel: false,
          confirmText: '我知道了'
        })
      }, 1500)
    },

    toggleRecording() {
      if (this.isRecording) {
        this.stopRecording()
      } else {
        this.startRecording()
      }
    },

    startRecording() {
      uni.showToast({ title: '开始录音', icon: 'success' })
      this.isRecording = true
      this.hasRecording = true
      this.recordingSeconds = 0
      this.recordingTime = '00:00'

      this.recordingTimer = setInterval(() => {
        this.recordingSeconds++
        const mins = Math.floor(this.recordingSeconds / 60).toString().padStart(2, '0')
        const secs = (this.recordingSeconds % 60).toString().padStart(2, '0')
        this.recordingTime = `${mins}:${secs}`
      }, 1000)
    },

    stopRecording() {
      if (this.recordingTimer) {
        clearInterval(this.recordingTimer)
        this.recordingTimer = null
      }
      this.isRecording = false
      this.audioDuration = this.recordingTime
      uni.showToast({ title: '录音已停止', icon: 'success' })
    },

    playRecording() {
      this.isPlaying = !this.isPlaying
      uni.showToast({ 
        title: this.isPlaying ? '开始播放' : '暂停播放', 
        icon: 'none' 
      })
    },

    saveRecording() {
      uni.showModal({
        title: '保存录音',
        content: '确定要保存该录音文件吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '保存成功', icon: 'success' })
          }
        }
      })
    },

    deleteRecording() {
      uni.showModal({
        title: '删除录音',
        content: '确定要删除该录音文件吗？',
        success: (res) => {
          if (res.confirm) {
            this.hasRecording = false
            this.recordingTime = '00:00'
            this.recordingSeconds = 0
            uni.showToast({ title: '已删除', icon: 'success' })
          }
        }
      })
    },

    goToAllRecords() {
      uni.showToast({ title: '全部记录功能开发中', icon: 'none' })
    },

    viewRecordDetail(record) {
      this.currentRecord = { ...record }
      this.showDetailModal = true
    },

    closeDetailModal() {
      this.showDetailModal = false
    },

    playRecordAudio(record) {
      uni.showToast({ title: '播放录音', icon: 'none' })
    },

    playCurrentRecordAudio() {
      this.isPlaying = !this.isPlaying
      uni.showToast({ 
        title: this.isPlaying ? '开始播放' : '暂停播放', 
        icon: 'none' 
      })
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
.emergency-container {
  min-height: 100vh;
  background: $background-color;
  padding-bottom: 40rpx;
}

.warning-banner {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  padding: 24rpx 30rpx;
}

.warning-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}

.warning-text {
  font-size: 26rpx;
  color: $danger-color;
  font-weight: 500;
}

.quick-action-section {
  display: flex;
  justify-content: center;
  padding: 60rpx 30rpx;
  background: $white;
  margin-bottom: 20rpx;
}

.emergency-btn {
  width: 300rpx;
  height: 300rpx;
  background: linear-gradient(135deg, #FF5722 0%, #E53935 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 40rpx rgba(244, 67, 54, 0.4);
  animation: pulse-red 2s infinite;
}

@keyframes pulse-red {
  0%, 100% {
    box-shadow: 0 10rpx 40rpx rgba(244, 67, 54, 0.4);
  }
  50% {
    box-shadow: 0 10rpx 60rpx rgba(244, 67, 54, 0.6);
  }
}

.btn-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.btn-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.btn-text {
  font-size: 36rpx;
  font-weight: 600;
  color: $white;
  margin-bottom: 8rpx;
}

.btn-subtext {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.recording-section {
  background: $white;
  margin-bottom: 20rpx;
  padding: 0 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 0;
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

.recording-card {
  padding-bottom: 30rpx;
}

.recording-status {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
  border-radius: 16rpx;
  margin-bottom: 24rpx;
}

.status-dot {
  width: 20rpx;
  height: 20rpx;
  background: $danger-color;
  border-radius: 50%;
  margin-right: 16rpx;
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.status-text {
  font-size: 28rpx;
  color: $danger-color;
  font-weight: 500;
  margin-right: 20rpx;
}

.recording-time {
  font-size: 32rpx;
  font-weight: 600;
  color: $danger-color;
  font-family: monospace;
}

.recording-info {
  padding: 24rpx;
  background: $background-color;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
}

.audio-preview {
  display: flex;
  align-items: center;
}

.audio-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.audio-progress {
  flex: 1;
}

.progress-bar {
  height: 8rpx;
  background: $border-color;
  border-radius: 4rpx;
  overflow: hidden;
  margin-bottom: 10rpx;
}

.progress-fill {
  width: 40%;
  height: 100%;
  background: $primary-color;
  border-radius: 4rpx;
}

.audio-time {
  display: flex;
  justify-content: space-between;
  font-size: 22rpx;
  color: $text-muted;
}

.audio-controls {
  margin-left: 20rpx;
}

.control-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  &.play {
    background: $primary-color;
  }
}

.control-icon {
  font-size: 28rpx;
}

.recording-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx;
  background: $background-color;
  border-radius: 16rpx;

  &.recording {
    background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  }
}

.action-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.action-text {
  font-size: 24rpx;
  color: $text-color;
}

.record-section {
  background: $white;
}

.record-list {
  padding: 0 30rpx;
  padding-bottom: 30rpx;
}

.record-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: $text-muted;
}

.record-item {
  background: $background-color;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.record-status {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  margin-bottom: 16rpx;

  &.pending {
    background: #FFF3E0;
    color: $warning-color;
  }

  &.processing {
    background: #E3F2FD;
    color: $primary-color;
  }

  &.completed {
    background: #E8F5E9;
    color: $success-color;
  }
}

.status-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.status-text {
  font-size: 24rpx;
}

.record-content {
  margin-bottom: 16rpx;
}

.record-info {
  display: flex;
  padding: 8rpx 0;
}

.info-label {
  font-size: 26rpx;
  color: $text-muted;
  min-width: 140rpx;
}

.info-value {
  font-size: 26rpx;
  color: $text-color;
  flex: 1;
}

.audio-link {
  color: $primary-color;
}

.record-detail {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-top: 16rpx;
  border-top: 1rpx solid $border-color;
}

.detail-text {
  font-size: 24rpx;
  color: $primary-color;
  margin-right: 8rpx;
}

.detail-arrow {
  font-size: 28rpx;
  color: $primary-color;
}

.emergency-detail-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  width: 90%;
  max-height: 80vh;
  background: $white;
  border-radius: 24rpx;
  overflow: hidden;
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
  padding: 30rpx;
  max-height: 60vh;
  overflow-y: auto;
}

.detail-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid $border-color;

  &:last-child {
    border-bottom: none;
  }
}

.detail-label {
  font-size: 28rpx;
  color: $text-muted;
  min-width: 180rpx;
}

.detail-value {
  font-size: 28rpx;
  color: $text-color;
  flex: 1;

  &.status {
    &.pending {
      color: $warning-color;
    }
    &.processing {
      color: $primary-color;
    }
    &.completed {
      color: $success-color;
    }
  }
}

.audio-player {
  display: flex;
  align-items: center;
}

.audio-name {
  flex: 1;
  font-size: 26rpx;
  color: $text-color;
}

.audio-play-btn {
  padding: 10rpx 24rpx;
  background: $primary-color;
  color: $white;
  border-radius: 30rpx;
  font-size: 24rpx;
}
</style>
