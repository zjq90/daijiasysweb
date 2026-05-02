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

<style>
/* 淡绿色主题配色 */
:root {
	--primary-color: #4CAF50;
	--primary-light: #E8F5E9;
	--primary-dark: #388E3C;
	--accent-color: #8BC34A;
	--text-primary: #333333;
	--text-secondary: #666666;
	--text-light: #999999;
	--bg-primary: #FFFFFF;
	--bg-secondary: #F5F5F5;
	--border-color: #E0E0E0;
	--success-color: #4CAF50;
	--warning-color: #FF9800;
	--error-color: #F44336;
	--info-color: #2196F3;
}

/* 全局样式 */
page {
	background-color: var(--bg-secondary);
	font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
	color: var(--text-primary);
	font-size: 28rpx;
	line-height: 1.6;
}

/* 容器 */
.container {
	padding: 30rpx;
	box-sizing: border-box;
}

/* 卡片样式 */
.card {
	background-color: var(--bg-primary);
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

/* 按钮样式 */
.btn {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 88rpx;
	border-radius: 44rpx;
	font-size: 30rpx;
	font-weight: 500;
	border: none;
}

.btn-primary {
	background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
	color: #FFFFFF;
	box-shadow: 0 8rpx 20rpx rgba(76, 175, 80, 0.3);
}

.btn-primary:active {
	opacity: 0.9;
	transform: translateY(2rpx);
}

.btn-outline {
	background-color: transparent;
	color: var(--primary-color);
	border: 2rpx solid var(--primary-color);
}

.btn-outline:active {
	background-color: var(--primary-light);
}

.btn-danger {
	background-color: var(--error-color);
	color: #FFFFFF;
}

.btn-success {
	background-color: var(--success-color);
	color: #FFFFFF;
}

.btn-small {
	height: 60rpx;
	font-size: 26rpx;
	border-radius: 30rpx;
	padding: 0 30rpx;
}

/* 输入框样式 */
.input-group {
	margin-bottom: 30rpx;
}

.input-label {
	display: block;
	font-size: 28rpx;
	color: var(--text-secondary);
	margin-bottom: 12rpx;
}

.input-field {
	width: 100%;
	height: 88rpx;
	background-color: var(--bg-secondary);
	border-radius: 12rpx;
	padding: 0 24rpx;
	font-size: 30rpx;
	border: none;
	box-sizing: border-box;
}

.input-field:focus {
	background-color: var(--primary-light);
}

/* 分割线 */
.divider {
	height: 1rpx;
	background-color: var(--border-color);
	margin: 24rpx 0;
}

/* 列表项 */
.list-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 24rpx 0;
	border-bottom: 1rpx solid var(--border-color);
}

.list-item:last-child {
	border-bottom: none;
}

.list-item-left {
	display: flex;
	align-items: center;
}

.list-item-icon {
	width: 40rpx;
	height: 40rpx;
	margin-right: 20rpx;
}

.list-item-text {
	font-size: 30rpx;
	color: var(--text-primary);
}

.list-item-right {
	display: flex;
	align-items: center;
	color: var(--text-light);
	font-size: 26rpx;
}

.arrow-right {
	width: 24rpx;
	height: 24rpx;
	border-top: 3rpx solid var(--text-light);
	border-right: 3rpx solid var(--text-light);
	transform: rotate(45deg);
	margin-left: 12rpx;
}

/* 徽章 */
.badge {
	display: inline-block;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
	font-size: 22rpx;
	font-weight: 500;
}

.badge-success {
	background-color: var(--primary-light);
	color: var(--primary-color);
}

.badge-warning {
	background-color: #FFF3E0;
	color: var(--warning-color);
}

.badge-danger {
	background-color: #FFEBEE;
	color: var(--error-color);
}

.badge-info {
	background-color: #E3F2FD;
	color: var(--info-color);
}

/* 空状态 */
.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
}

.empty-icon {
	width: 200rpx;
	height: 200rpx;
	margin-bottom: 30rpx;
	opacity: 0.5;
}

.empty-text {
	font-size: 28rpx;
	color: var(--text-light);
}

/* 浮动按钮 */
.fab {
	position: fixed;
	right: 40rpx;
	bottom: 200rpx;
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 8rpx 24rpx rgba(76, 175, 80, 0.4);
	z-index: 999;
}

.fab-icon {
	width: 48rpx;
	height: 48rpx;
}

/* 开关样式 */
.switch {
	width: 96rpx;
	height: 56rpx;
}

/* 标题样式 */
.section-title {
	font-size: 32rpx;
	font-weight: 600;
	color: var(--text-primary);
	margin-bottom: 24rpx;
}

/* 统计卡片 */
.stat-card {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	flex: 1;
}

.stat-value {
	font-size: 48rpx;
	font-weight: 600;
	color: var(--primary-color);
	margin-bottom: 8rpx;
}

.stat-label {
	font-size: 24rpx;
	color: var(--text-secondary);
}
</style>
