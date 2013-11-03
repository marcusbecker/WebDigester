/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.script;

/**
 *
 * @author mbecker
 */
public interface IProcessor<T, V> {

    public T process(V in);
}
