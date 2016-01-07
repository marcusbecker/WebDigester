/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.script;

import br.com.mvbos.webdigester.core.Element;
import java.util.List;

/**
 *
 * @author Marcus Becker
 */
public interface IProcessProgress {

    public int getStatus();

    public boolean isDone();

    public List<Element> getResult();
}
