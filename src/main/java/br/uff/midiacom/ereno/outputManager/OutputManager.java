/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.outputManager;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.outputManager.model.Detail;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import br.uff.midiacom.ereno.outputManager.model.Error;

/**
 *
 * @author silvio
 */
public interface OutputManager {

   
    OutputManager initialize(String graspMethod);

    void writeError(Error error);

    void writeDetail(Detail detail);

    void writeIteration(Iteration iteration);

}
