package com.jcvanz.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    // vai pegar todas as propriedades nulas e faz a conversão para mesclar as informações
    public static void copyNonNullProperties(Object source, Object target) {
        // copia as propriedades nulas
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    // método que vai retornar um array com todos os nomes das propriedades nulas
    public static String[] getNullPropertyNames(Object source) {
        // BeanWapper = interface que vai permitir acessar as propriedades de um objeto / BeanWrapperImpl = é a implementação dessa interface
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        // obtem as propriedades e gera um array
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // cria um conjunto com as propriendades com valores nulos
        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd: pds) {
            // obtem o valor da propriedade atual
            Object srcValue = src.getPropertyValue(pd.getName());
            // faz a verificação se a propriedade é null
            if(srcValue == null) {
                // add ao array
                emptyNames.add(pd.getName());
            }
        }
        // cria um array de string para add todas as propriedade nulas
        String[] result = new String[emptyNames.size()];
        
        // retorna o array
        return emptyNames.toArray(result);
    }
}
