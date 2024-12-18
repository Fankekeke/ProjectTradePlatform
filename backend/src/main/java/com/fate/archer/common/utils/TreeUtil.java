package com.fate.archer.common.utils;

import com.fate.archer.common.domain.Tree;
import com.fate.archer.common.domain.router.RouterMeta;
import com.fate.archer.common.domain.router.VueRouter;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    protected TreeUtil() {

    }

    private final static String TOP_NODE_ID = "0";

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pid = node.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null)
                        n.initChildren();
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });


        Tree<T> root = new Tree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        root.setText("root");
        return root;
    }


    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter<T>>
     */
    public static <T> ArrayList<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();
        router.setName("系统主页");
        router.setPath("/home");
        router.setComponent("HomePageView");
        router.setIcon("home");
        router.setChildren(null);
        router.setMeta(new RouterMeta(false, true));
        topRoutes.add(router);

        routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                String id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null)
                        parent.initChildren();
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
//        router = new VueRouter<>();
//        router.setPath("/profile");
//        router.setName("个人中心");
//        router.setComponent("personal/Profile");
//        router.setIcon("none");
//        router.setMeta(new RouterMeta(true, false));
//        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/personal");
        router.setName("个人信息");
        router.setComponent("user/personal/Personal");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/message");
        router.setName("我的消息");
        router.setComponent("user/message/Message");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/order");
        router.setName("我的订单");
        router.setComponent("user/order/Order");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/apartment");
        router.setName("申请邀请人");
        router.setComponent("user/apartment/Apartment");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/balance");
        router.setName("余额统计");
        router.setComponent("user/balance/Balance");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/product");
        router.setName("产品展示");
        router.setComponent("business/product/Product");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/annex");
        router.setName("论文附件");
        router.setComponent("business/annex/Annex");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/team");
        router.setName("我们团队");
        router.setComponent("business/team/Team");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/news");
        router.setName("资讯中心");
        router.setComponent("business/news/News");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        router = new VueRouter<>();
        router.setPath("/custom");
        router.setName("项目定制");
        router.setComponent("business/custom/Custom");
        router.setIcon("none");
        router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);

        ArrayList<VueRouter<T>> list = new ArrayList<>();
        VueRouter<T> root = new VueRouter<>();
        root.setName("主页");
        root.setComponent("MenuView");
        root.setIcon("none");
        root.setPath("/");
        root.setRedirect("/home");
        root.setChildren(topRoutes);
        list.add(root);

        root = new VueRouter<>();
        root.setName("404");
        root.setComponent("error/404");
        root.setPath("*");
        list.add(root);

        return list;
    }
}