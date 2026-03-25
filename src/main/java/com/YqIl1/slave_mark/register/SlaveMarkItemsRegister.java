package com.YqIl1.slave_mark.register;

import com.YqIl1.slave_mark.SlaveMark;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import com.YqIl1.slave_mark.item.BaseBlessingItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SlaveMarkItemsRegister {
    // 创建 DeferredRegister
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SlaveMark.MODID);
    // 注册奴隶烙印物品
    public static void AutoRegisterItems(){
        String packageName = "com.YqIl1.slave_mark.item";
        try{
            List<Class<?>> classes = getClasses(packageName);
            for(Class<?> clazz : classes){
                if (clazz.isAnnotationPresent(SlaveMarkItems.class)){
                    SlaveMarkItems annotation = clazz.getAnnotation(SlaveMarkItems.class);
                    String registryId = annotation.id();

                    ITEMS.register(registryId,()->{
                        try{
                            return (Item) clazz.getDeclaredConstructor().newInstance();
                        } catch(Exception e){
                            throw new RuntimeException("实例化失败: " + registryId,e);
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        net.minecraftforge.fml.loading.moddiscovery.ModFileInfo modFileInfo =
                net.minecraftforge.fml.loading.FMLLoader.getLoadingModList().getModFileById(SlaveMark.MODID);

        if (modFileInfo == null) return classes;

        java.nio.file.Path rootPath = modFileInfo.getFile().findResource(path);

        if (java.nio.file.Files.exists(rootPath)) {
            //改为递归扫描所有子目录
            java.nio.file.Files.walk(rootPath).forEach(p -> {
                String fileName = p.getFileName().toString();
                if (fileName.endsWith(".class")) {
                    try {
                        //计算文件相对于包根路径的相对路径，转换成包名
                        //item/minecraft/SwordSoulStone.class -> minecraft.SwordSoulStone
                        String relativePath = rootPath.relativize(p).toString()
                                .replace(java.io.File.separatorChar, '.') // 兼容不同操作系统的分隔符
                                .replace('/', '.'); // 确保 Linux/Jar 内部路径也正确转换

                        String className = packageName + "." + relativePath.substring(0, relativePath.length() - 6);

                        Class<?> clazz = Class.forName(className);
                        // 排除抽象类和接口
                        if (!clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return classes;
    }
}