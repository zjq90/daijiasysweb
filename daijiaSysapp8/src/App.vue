<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style lang="scss">
/*每个页面公共css */
@import './uni.scss';

page {
  background-color: $background-color;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 28rpx;
  color: $text-color;
  min-height: 100vh;
}

.container {
  padding: 20rpx;
  box-sizing: border-box;
}

.card {
  background-color: $white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
  background-color: $primary-color;
  color: $white;
  border-radius: 50rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 32rpx;
  font-weight: 500;
}

.btn-primary:active {
  background-color: $primary-dark;
}

.btn-outline {
  background-color: transparent;
  color: $primary-color;
  border: 2rpx solid $primary-color;
  border-radius: 50rpx;
  height: 88rpx;
  line-height: 84rpx;
  text-align: center;
  font-size: 32rpx;
  font-weight: 500;
}

.btn-outline:active {
  background-color: rgba(79, 195, 247, 0.1);
}

.text-primary {
  color: $primary-color;
}

.text-muted {
  color: $text-muted;
}

.text-success {
  color: $success-color;
}

.text-warning {
  color: $warning-color;
}

.text-danger {
  color: $danger-color;
}

.flex {
  display: flex;
}

.flex-column {
  flex-direction: column;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-around {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.mt-10 {
  margin-top: 10rpx;
}

.mt-20 {
  margin-top: 20rpx;
}

.mt-30 {
  margin-top: 30rpx;
}

.mb-10 {
  margin-bottom: 10rpx;
}

.mb-20 {
  margin-bottom: 20rpx;
}

.mb-30 {
  margin-bottom: 30rpx;
}

.p-20 {
  padding: 20rpx;
}

.p-30 {
  padding: 30rpx;
}

.border-bottom {
  border-bottom: 1rpx solid $border-color;
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ellipsis-2 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
