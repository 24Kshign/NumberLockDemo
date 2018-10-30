# NumberLockDemo
仿ios的数字锁屏

## 前言：
话不多说，先上动图

![第一种](http://upload-images.jianshu.io/upload_images/490111-d1e24f303608ae55.gif?imageMogr2/auto-orient/strip)

![第二种](http://upload-images.jianshu.io/upload_images/490111-9c16ff055888f5ea.gif?imageMogr2/auto-orient/strip)

### 序言：这两天老大给了个任务，说是做一个仿ios的数字锁屏界面，心想着这种东西网上应该有挺多的，然后就先百度了一把，谁知道案例好像少的可怜，然后带着怀疑的心态去下载了千辛万苦找到的“源码”，看里面写的，然后自己有点眉目了，就自己借着“源码”的思路自己实现了一把，见上图。

>#### 思路：
>这里我们可以看成两部分，一部分是上面的输入的，另一部分是底部的按键。
>先来看上面那部分，我们可以看成是TextView，然后响应下面按键的动作。下面这部分，图中的每个按钮都需要自己画出来，难点就是根据第一个按键的坐标(第一个坐标我们初始化)算出每个按键的坐标，然后根据手指的触摸屏事件来判断点击的是哪个按键

接下来我们来看核心代码：

**输入框部分：**

	public class PasswordTextView extends TextView{
	    private final String sing = "*";//密文显示的内容
	    private String content = "";//显示的内容
	    //文本改变事件回调接口
	    private OnTextChangedListener onTextChangedListener;
	    /**
	     * Handler线程对象,用来更新密码框的显示内容
	     * 实现将输入的内容显示为密文
	     */
	    private Handler handler = new Handler(){
	        public void handleMessage(android.os.Message msg) {
	            //密文显示
	            PasswordTextView.this.setText(sing);
	            //回调改变事件接口
	            if(onTextChangedListener != null){
	                onTextChangedListener.textChanged(content);
	            }
	        };
	    };
	
	    /**
	     * 构造方法
	     * @param context
	     * @param attrs
	     */
	    public PasswordTextView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	
	    /**
	     * 设置文本改变事件监听
	     * @param onTextChangedListener
	     */
	    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
	        this.onTextChangedListener = onTextChangedListener;
	    }
	
	    /**
	     * 设置密码框显示的内容
	     * @param text
	     */
	    public void setTextContent(String text){
	        //获得输入的内容
	        this.content = text;
	        if(!TextUtils.isEmpty(text)){
	            handler.sendEmptyMessage(0);//向Handler发送消息
	        }else{
	            this.setText("");
	        }
	    }
	
	    /**
	     * 获取显示的内容
	     * @return
	     */
	    public String getTextContent(){
	        return content;
	    }
	
	    /**
	     * 文本改变事件接口
	     */
	    public interface OnTextChangedListener{
	        /**
	         * 密码框文本改变时调用
	         * @param content
	         */
	        public void textChanged(String content);
	    }
	}

**下面按键部分**

	public class NumericK	eyboard extends View {
	    private int screen_width = 0;// 屏幕的宽度
	    private float first_x = 0;// 绘制1的x坐标
	    private float first_y = 0;// 绘制1的y坐标
	    private float[] xs = new float[3];//声明数组保存每一列的圆心横坐标
	    private float[] ys = new float[4];//声明数组保存每一排的圆心纵坐标
	    private float circle_x, circle_y;//点击处的圆心坐标
	    private int number = -1;//点击的数字
	    private OnNumberClick onNumberClick;//数字点击事件
	    /*
	     * 判断刷新数据
	     * -1 不进行数据刷新
	     * 0  按下刷新
	     * 1  弹起刷新
	     */
	    private int type = -1;
	
	    /**
	     * 构造方法
	     *
	     * @param context
	     */
	    public NumericKeyboard(Context context) {
	        super(context);
	        initData(context);// 初始化数据
	    }
	
	    public NumericKeyboard(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initData(context);// 初始化数据
	    }
	
	    /**
	     * 设置数字点击事件
	     *
	     * @param onNumberClick
	     */
	    public void setOnNumberClick(OnNumberClick onNumberClick) {
	        this.onNumberClick = onNumberClick;
	    }
	
	    // 初始化数据
	    private void initData(Context context) {
	        // 获取屏幕的宽度
	        screen_width = SystemUtils.getSystemDisplay(context)[0];
	        // 获取绘制1的x坐标
	        first_x = screen_width / 4;
	        // 获取绘制1的y坐标
	        first_y = (SystemUtils.getSystemDisplay(context)[1] - SystemUtils.getSystemDisplay(context)[1] / 3) / 4;
	        //添加每一排的横坐标
	        xs[0] = first_x + 10;
	        xs[1] = first_x * 2 + 10;
	        xs[2] = first_x * 3 + 10;
	        //添加每一列的纵坐标
	        ys[0] = 40 + first_y - 15;
	        ys[1] = 40 + first_y + first_x - 15;
	        ys[2] = 40 + first_y + first_x * 2 - 15;
	        ys[3] = 40 + first_y + first_x * 3 - 15;
	    }
	
	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        // 创建画笔对象
	        Paint paint = new Paint();
	        paint.setColor(Color.BLACK);// 设置画笔颜色
	        paint.setTextSize(40);// 设置字体大小
	        paint.setStrokeWidth(2);
	        // 绘制文本,注意是从坐标开始往上绘制
	        // 这里较难的就是算坐标
	        // 绘制第一排1,2,3
	        canvas.drawText("1", first_x, 40 + first_y, paint);
	        canvas.drawText("2", first_x * 2, 40 + first_y, paint);
	        canvas.drawText("3", first_x * 3, 40 + first_y, paint);
	        // 绘制第2排4,5,6
	        canvas.drawText("4", first_x, 40 + first_y + first_x, paint);
	        canvas.drawText("5", first_x * 2, 40 + first_y + first_x, paint);
	        canvas.drawText("6", first_x * 3, 40 + first_y + first_x, paint);
	        // 绘制第3排7,8,9
	        canvas.drawText("7", first_x, 40 + first_y + first_x * 2, paint);
	        canvas.drawText("8", first_x * 2, 40 + first_y + first_x * 2, paint);
	        canvas.drawText("9", first_x * 3, 40 + first_y + first_x * 2, paint);
	        // 绘制第4排0
	        canvas.drawText("0", first_x * 2, 40 + first_y + first_x * 3, paint);
	        //为每一个数字绘制一个圆
	        paint.setColor(Color.WHITE);//设置画笔颜色
	        paint.setAntiAlias(true);//设置抗锯齿
	        //设置绘制空心圆
	        paint.setStyle(Paint.Style.STROKE);
	        //依次绘制第一排的圆
	        canvas.drawCircle(first_x + 10, 40 + first_y - 15, 70, paint);
	        canvas.drawCircle(first_x * 2 + 10, 40 + first_y - 15, 70, paint);
	        canvas.drawCircle(first_x * 3 + 10, 40 + first_y - 15, 70, paint);
	        //依次绘制第2排的圆
	        canvas.drawCircle(first_x + 10, 40 + first_y + first_x - 15, 70, paint);
	        canvas.drawCircle(first_x * 2 + 10, 40 + first_y + first_x - 15, 70, paint);
	        canvas.drawCircle(first_x * 3 + 10, 40 + first_y + first_x - 15, 70, paint);
	        //依次绘制第3排的圆
	        canvas.drawCircle(first_x + 10, 40 + first_y + first_x * 2 - 15, 70, paint);
	        canvas.drawCircle(first_x * 2 + 10, 40 + first_y + first_x * 2 - 15, 70, paint);
	        canvas.drawCircle(first_x * 3 + 10, 40 + first_y + first_x * 2 - 15, 70, paint);
	        //绘制最后一个圆
	        canvas.drawCircle(first_x * 2 + 10, 40 + first_y + first_x * 3 - 15, 70, paint);
	
	        //判断是否点击数字(点击数字产生的渐变效果)
	        if (circle_x > 0 && circle_y > 0) {
	            if (type == 0) {//按下刷新
	                paint.setColor(Color.WHITE);//设置画笔颜色
	                paint.setStyle(Paint.Style.FILL_AND_STROKE);//按下的时候绘制实心圆
	                canvas.drawCircle(circle_x, circle_y, 70, paint);//绘制圆
	            } else if (type == 1) {//弹起刷新
	                paint.setColor(Color.WHITE);//设置画笔颜色
	                paint.setStyle(Paint.Style.STROKE);//弹起的时候再绘制空心圆
	                canvas.drawCircle(circle_x, circle_y, 70, paint);//绘制圆
	                //绘制完成后,重置
	                circle_x = 0;
	                circle_y = 0;
	            }
	        }
	    }
	
	    /**
	     * 获取触摸点击事件
	     */
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        //事件判断
	        switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN://按下
	                //判断点击的坐标位置
	                float x = event.getX();//按下时的X坐标
	                float y = event.getY();//按下时的Y坐标
	                //判断点击的是哪一个数字圆
	                handleDown(x, y);
	                return true;
	            case MotionEvent.ACTION_UP://弹起
	                type = 1;//弹起刷新
	                invalidate();//刷新界面
	                //返回点击的数字
	                if (onNumberClick != null && number != -1) {
	                    onNumberClick.onNumberReturn(number);
	                }
	                setDefault();//恢复默认
	                //发送辅助事件
	                sendAccessEvent(R.string.numeric_keyboard_up);
	                return true;
	            case MotionEvent.ACTION_CANCEL://取消
	                //恢复默认值
	                setDefault();
	                return true;
	        }
	        return false;
	    }
	
	    /*
	     * 恢复默认值
	     */
	    private void setDefault() {
	        circle_x = 0;
	        circle_y = 0;
	        type = -1;
	        number = -1;
	        sendAccessEvent(R.string.numeric_keyboard_cancel);
	    }
	
	    /*
	     * 设置辅助功能描述
	     */
	    private void sendAccessEvent(int resId) {
	        //设置描述
	        setContentDescription(getContext().getString(resId));
	        //发送辅助事件
	        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
	        setContentDescription(null);
	    }
	
	    /*
	     * 判断点击的是哪一个数字圆
	     */
	    private void handleDown(float x, float y) {
	        //判断点击的是那一列的数据
	        if (xs[0] - 70 <= x && x <= xs[0] + 70) {//第一列
	            //获取点击处的圆心横坐标
	            circle_x = xs[0];
	            //判断点击的是哪一排
	            if (ys[0] - 70 <= y && ys[0] + 70 >= y) {//第1排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[0];
	                number = 1;//设置点击的数字
	            } else if (ys[1] - 70 <= y && ys[1] + 70 >= y) {//第2排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[1];
	                number = 4;//设置点击的数字
	            } else if (ys[2] - 70 <= y && ys[2] + 70 >= y) {//第3排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[2];
	                number = 7;//设置点击的数字
	            }
	        } else if (xs[1] - 70 <= x && x <= xs[1] + 70) {//第2列
	            //获取点击处的圆心横坐标
	            circle_x = xs[1];
	            //判断点击的是哪一排
	            if (ys[0] - 70 <= y && ys[0] + 70 >= y) {//第1排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[0];
	                number = 2;//设置点击的数字
	            } else if (ys[1] - 70 <= y && ys[1] + 70 >= y) {//第2排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[1];
	                number = 5;//设置点击的数字
	            } else if (ys[2] - 70 <= y && ys[2] + 70 >= y) {//第3排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[2];
	                number = 8;//设置点击的数字
	            } else if (ys[3] - 70 <= y && ys[3] + 70 >= y) {//第4排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[3];
	                number = 0;//设置点击的数字
	            }
	        } else if (xs[2] - 70 <= x && x <= xs[2] + 70) {//第3列
	            //获取点击处的圆心横坐标
	            circle_x = xs[2];
	            //判断点击的是哪一排
	            if (ys[0] - 70 <= y && ys[0] + 70 >= y) {//第1排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[0];
	                number = 3;//设置点击的数字
	            } else if (ys[1] - 70 <= y && ys[1] + 70 >= y) {//第2排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[1];
	                number = 6;//设置点击的数字
	            } else if (ys[2] - 70 <= y && ys[2] + 70 >= y) {//第3排
	                //获取点击的数字圆的圆心纵坐标
	                circle_y = ys[2];
	                number = 9;//设置点击的数字
	            }
	        }
	        sendAccessEvent(R.string.numeric_keyboard_down);
	        type = 0;//按下刷新
	        //绘制点击时的背景圆
	        invalidate();
	    }
	
	    /**
	     * 数字点击事件
	     */
	    public interface OnNumberClick {
	        /**
	         * 返回点击的数字
	         *
	         * @param number
	         */
	        public void onNumberReturn(int number);
	    }
	}

上面说了，难点在于计算按键的位置，在这里我是根据我下载的demo里面的计算方式相应的修改了，如果不明白android屏幕坐标系的同学请看下面的文章：

>#### 参考：

>[android坐标系详解](http://blog.csdn.net/nicolasyan/article/details/50943073)

![公众号：Android技术经验分享](http://upload-images.jianshu.io/upload_images/490111-8c1cdb3bd9dfd604.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
