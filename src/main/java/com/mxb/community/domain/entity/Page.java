package com.mxb.community.domain.entity;

/**
 * 封装分页相关信息
 * current 当前页码
 * limit 显示上限
 * rows 数据总数(用于计算总页数)
 * path 查询路径(用于复用分页连接)
 */
public class Page {

    private int current = 1;

    private int limit = 10;

    private int rows;

    private String path;


    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总的页数
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 显示前两页
     * @return
     */
    public int getFrom() {
        if (current - 2 < 1) {
            return 1;
        } else {
            return current - 2;
        }
    }

    /**
     * 显示后两页
     * @return
     */
    public int getTo() {
        if (current + 2 > getTotal()) {
            return getTotal();
        } else {
            return current + 2;
        }
    }

}
