package com.peixing.recyclerviewdemo.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peixing.recyclerviewdemo.R;
import com.peixing.recyclerviewdemo.utils.DiskLruCacheUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private static final String TAG = "GridAdapter";
    private Context context;
    //记录所有正在下载或等再下载的任务
    private Set<BitmapWorkerTask> taskCollection;

    //图片内存缓存技术的核心类，用于缓存所有下载好的图片
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 图片硬盘缓存核心类
     */
    private DiskLruCache mDiskLruCache;
    //每个条目的高度
    private int mItemHeight = 10;
    String[] resources;
    RecyclerView recyclerView;


    public GridAdapter(Context context, String[] resources, RecyclerView recyclerView) {
        this.context = context;
        this.resources = resources;
        this.recyclerView = recyclerView;

        taskCollection = new HashSet<BitmapWorkerTask>();
        //获取应用程序的最大可用缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置图片缓存大小为程序最大可用内存的1/8
        int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            //获取图片缓存路径,主要区分是否有外接存储卡
            File cacheDir = getDiskCacheDir(context, "thumb");
            if (!cacheDir.exists()) {
                //目录不能存在，立即创建
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 根据传入的thumb 获取硬盘缓存的路径地址
     *
     * @param context
     * @param thumb
     * @return
     */
    private File getDiskCacheDir(Context context, String thumb) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //内部存储
            cachePath = context.getCacheDir().getPath();
        }
//        Log.i(TAG, "getDiskCacheDir: " + cachePath + File.separator + thumb);
        return new File(cachePath + File.separator + thumb);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, int position) {
        if (itemViewHolder.ivPhoto.getLayoutParams().height != mItemHeight) {
            itemViewHolder.ivPhoto.getLayoutParams().height = mItemHeight;
        }
        itemViewHolder.ivPhoto.setTag(resources[position]);
        loadBitmaps(itemViewHolder.ivPhoto, resources[position]);
//        DiskLruCacheUtil.LoadBitmaps(itemViewHolder.ivPhoto, resources[position]);
        //Here you can fill your row view
    }


    @Override
    public int getItemCount() {
        return resources.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);

        }
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();

    }

    /**
     * 加载图片
     *
     * @param ivPhoto
     * @param resource
     */
    private void loadBitmaps(ImageView ivPhoto, String resource) {
        try {
            Bitmap bitmap = getBitmapFromMemoryCache(resource);
//            Log.i(TAG, "loadBitmaps: " + resource);
            if (bitmap == null) {
                BitmapWorkerTask task = new BitmapWorkerTask();
                taskCollection.add(task);
                task.execute(resource);
            } else {
                if (ivPhoto != null && bitmap != null) {

                    ivPhoto.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将Bitmap对象添加到内存缓存中
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache 取出一战图片，如果不存在就返回null
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }


    /**
     * 取消所有正在下载或等待下载的任务
     */
    public void cancelAllTasks() {

        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }


    //根据url得到hashcode
    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节转二进制字符
     *
     * @param bytes
     * @return
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 刷新目录，将索引加入jounral文件
     */
    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
            }
        }
    }


    /**
     * 异步下载图片的任务
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream = null;
            DiskLruCache.Snapshot snapShot = null;
            try {
                //生成图片url对应的key
//                Log.i(TAG, "loadBitmaps: " + imageUrl);
                String key = hashKeyForDisk(imageUrl);
                //查找key对应的缓存
                snapShot = mDiskLruCache.get(key);
                if (snapShot == null) {
                    // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(imageUrl, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    //写入缓存后，再次查找对应的缓存
                    snapShot = mDiskLruCache.get(key);
                }
                if (snapShot != null) {
                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                Bitmap bitmap = null;
                if (fileDescriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap != null) {
                    addBitmapToMemoryCache(params[0], bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) recyclerView.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }


        /**
         * 从网络下载文件写入缓存
         *
         * @param imageUrl
         * @param outputStream
         * @return
         */
        private boolean downloadUrlToStream(String imageUrl, OutputStream outputStream) {
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                URL url = new URL(imageUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
