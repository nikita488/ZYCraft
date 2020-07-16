function initializeCoreMod() {
    return {
        'check_cloud_sprite': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.texture.AtlasTexture',
                'methodName': 'func_215256_a',
                'methodDesc': '(Lnet/minecraft/resources/IResourceManager;Ljava/util/Set;)Ljava/util/Collection;'
            },
            'transformer': function(method) {
                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                ASMAPI.log('INFO', 'Adding \'check_cloud_sprite\' ASM patch...');

                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');

                var insn = ASMAPI.findFirstInstruction(method, Opcodes.IFNE);
                var insnList = new InsnList();

                insnList.add(ASMAPI.buildMethodCall('nikita488/zycraft/client/texture/CloudSprite', 'name', '()Lnet/minecraft/util/ResourceLocation;', ASMAPI.MethodType.STATIC));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 6));
                insnList.add(ASMAPI.buildMethodCall('net/minecraft/util/ResourceLocation', 'equals', '(Ljava/lang/Object;)Z', ASMAPI.MethodType.VIRTUAL));
                insnList.add(new JumpInsnNode(Opcodes.IFNE, insn.label));

                method.instructions.insert(insn, insnList);

                ASMAPI.log('INFO', 'Added \'check_cloud_sprite\' ASM patch!');
                return method;
            }
        },
        'add_cloud_sprite_info': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.texture.AtlasTexture',
                'methodName': 'func_229220_a_',
                'methodDesc': '(Lnet/minecraft/resources/IResourceManager;Ljava/util/stream/Stream;Lnet/minecraft/profiler/IProfiler;I)Lnet/minecraft/client/renderer/texture/AtlasTexture$SheetData;'
            },
            'transformer': function(method) {
                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                ASMAPI.log('INFO', 'Adding \'add_cloud_sprite_info\' ASM patch...');

                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

                var addSpriteName = ASMAPI.mapMethod('func_229211_a_');

                var addSprite = ASMAPI.findFirstMethodCallBefore(method,
                        ASMAPI.MethodType.VIRTUAL,
                        'net/minecraft/client/renderer/texture/Stitcher',
                        addSpriteName,
                        '(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;)V',
                        method.instructions.size() - 1);

                method.instructions.insert(addSprite, ASMAPI.buildMethodCall(
                    'nikita488/zycraft/asm/ASMHooks',
                    'addCloudSpriteInfo',
                    '(Lnet/minecraft/client/renderer/texture/AtlasTexture;Lnet/minecraft/client/renderer/texture/Stitcher;)V',
                    ASMAPI.MethodType.STATIC));
                method.instructions.insert(addSprite, new VarInsnNode(Opcodes.ALOAD, 7));
                method.instructions.insert(addSprite, new VarInsnNode(Opcodes.ALOAD, 0));

                ASMAPI.log('INFO', 'Added \'add_cloud_sprite_info\' ASM patch!');
                return method;
            }
        },
        'create_cloud_sprite': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.texture.AtlasTexture',
                'methodName': 'lambda$getStitchedSprites$4',
                'methodDesc': '(ILjava/util/concurrent/ConcurrentLinkedQueue;Ljava/util/List;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIII)V'
            },
            'transformer': function(method) {
                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                ASMAPI.log('INFO', 'Adding \'create_cloud_sprite\' ASM patch...');

                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');

                var cloudSpriteInfo = ASMAPI.buildMethodCall('nikita488/zycraft/client/texture/CloudSprite', 'info', '()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;', ASMAPI.MethodType.STATIC)
                var createCloudSprite = ASMAPI.buildMethodCall(
                    'nikita488/zycraft/asm/ASMHooks',
                    'createCloudSprite',
                    '(Lnet/minecraft/client/renderer/texture/AtlasTexture;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;',
                    ASMAPI.MethodType.STATIC)
                var missingSprite = ASMAPI.findFirstInstruction(method, Opcodes.ASTORE);

                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                insnList.add(cloudSpriteInfo);
                insnList.add(new JumpInsnNode(Opcodes.IF_ACMPNE, method.instructions.get(0)));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 6));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 7));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 8));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 9));
                insnList.add(createCloudSprite);
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 10));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, missingSprite.getNext()));

                method.instructions.insert(insnList);

                ASMAPI.log('INFO', 'Added \'create_cloud_sprite\' ASM patch!');
                return method;
            }
        }
    }
}
