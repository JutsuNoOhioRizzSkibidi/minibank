package bank.adapters;

import bank.annotations.Audited;
import bank.annotations.RequiresRole;
import java.lang.reflect.Method;

public class AnnotationInspector{
    public void inspect(Class<?> type){
        System.out.println("класс: " + type.getName());
        if (type.isAnnotationPresent(Audited.class)){
            System.out.println("@Audited: есть");
        }
        else{
            System.out.println("@Audited: нет");
        }
        System.out.println("методы:");
        for (Method method : type.getDeclaredMethods()){
            System.out.println(method.getName());

            RequiresRole role = method.getAnnotation(RequiresRole.class);

            if (role != null){
                System.out.println(method.getName() + " -> " + role.value());
            }
        }
    }
}