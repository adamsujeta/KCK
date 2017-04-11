/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.Interface;

import java.util.List;

/**
 *
 * @author adams
 */
public interface CentralInterface {

    public String GetCurrentPath();

    public void SetCurrentPath(String path);

    public List<String[]> GetFolderContent();

    public void Delete();

    public void Copy();

    public void Paste(String path);

    public void Cut();

    public void RunFile();

}
