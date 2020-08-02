package fr.fogux.lift_simulator.batchs.core;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import fr.fogux.lift_simulator.batchs.graphs.BatchGraphProducer;
import fr.fogux.lift_simulator.exceptions.SimulateurException;
import fr.fogux.lift_simulator.fichiers.DataTagCompound;
import fr.fogux.lift_simulator.fichiers.GestFichiers;
import fr.fogux.lift_simulator.fichiers.TagNames;

public abstract class SimuBatch 
{
	protected final File dossier;
	protected final BatchThreadManager manager;
	
	public SimuBatch(File dossierDuBatch, long randomSeed)
	{
		this.dossier = dossierDuBatch;
		manager = new BatchThreadManager(GestFichiers.createBatchErrorLogDirectory(dossierDuBatch),randomSeed);
	}
	
	public void run()
	{
		runBatch();
		manager.shutdown();
	}
	
	protected abstract void runBatch();
	
	public static SimuBatch fromCompound(File dossierBatch,DataTagCompound batchConfig) throws IOException
	{
		String type = batchConfig.getString(TagNames.batchType);
		if(!batchConfig.hasKey(TagNames.seed))
		{
			batchConfig.setLong(TagNames.seed, new Random().nextLong());
		}
		switch (type)
		{
			case BatchGraphProducer.Name:
				return new BatchGraphProducer(dossierBatch,batchConfig);
			default:
				throw new SimulateurException("btach type " + type + " is unknown");
		}
	}
}
